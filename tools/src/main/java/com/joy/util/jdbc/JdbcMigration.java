package com.joy.util.jdbc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class JdbcMigration {

	private static final String sqlFolder = "./sqls";

	private static final String migrateTable = "migrations";

	private static final String DEFAULT_DELIMITER = ";";

	public static void migrate(String url, String user, String password) {
		try (Connection con = DriverManager.getConnection(url, user, password)) {
			if (con != null) {
				// 判斷migration表是否存在
				Statement statement = con.createStatement();
				try {
					statement.executeQuery("select 1 from " + migrateTable);
				} catch (SQLException e1) {
					System.out.println("migrate table not exist .create table...");
					// 不存在創建表
					statement.execute("create table " + migrateTable + "(id bigint(20) not null primary key)");
				} finally {
					if (statement != null) {
						statement.close();
					}
				}

				// 开始执行migration
				File dir = new File(sqlFolder);
				if (!dir.exists()) {
					System.out.println("no sql need migrate..");
					return;
				}
				migrate(con, dir);

			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.exit(0);
		}
	}

	private static void migrate(Connection con, File dir) throws Exception {
		File[] files = dir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return dir.isDirectory() || name.endsWith(".sql");
			}
		});
		if (files == null || files.length <= 0) {
			return;
		}
		Collections.sort(Arrays.asList(files), new Comparator<File>() {
			@Override
			public int compare(File o1, File o2) {
				if (o1.isDirectory()) {
					return 1;
				}
				if (o2.isDirectory()) {
					return -1;
				}
				String name1 = o1.getName();
				String name2 = o2.getName();
				long t1 = Long.parseLong(name1.substring(name1.lastIndexOf("_") + 1, name1.lastIndexOf(".")));
				long t2 = Long.parseLong(name2.substring(name2.lastIndexOf("_") + 1, name1.lastIndexOf(".")));
				if (t1 == t2) {
					throw new IllegalArgumentException(
							"file[" + name1 + "] and file[" + name2 + "] has same timestamp");
				}
				return (int) (t1 - t2);
			}
		});
		for (File file : files) {
			if (file.isFile()) {
				String name = file.getName();
				long t = Long.parseLong(name.substring(name.lastIndexOf("_") + 1, name.lastIndexOf(".")));

				// 判斷是否已經執行过
				PreparedStatement statement = con
						.prepareStatement("select count(*) from " + migrateTable + " where id=?");
				statement.setLong(1, t);
				ResultSet rs = statement.executeQuery();
				if (rs.next() && rs.getLong(1) <= 0) {
					rs.close();
					statement.close();
					System.out.println("begin migrate sql file:" + file.getName());
					runScript(con, t, new FileReader(file));
					System.out.println("end migrate sql file:" + file.getName());
				}

			} else {
				migrate(con, file);
			}
		}
	}

	private static void runScript(Connection conn, long t, Reader reader) throws Exception {
		conn.setAutoCommit(false);
		StringBuilder command = new StringBuilder();
		try (BufferedReader lineReader = new BufferedReader(reader)) {
			String line = null;
			boolean comment = false;
			while ((line = lineReader.readLine()) != null) {
				String trimmedLine = line.trim();
				if (trimmedLine.isEmpty() || trimmedLine.startsWith("--")) {
					continue;
				}

				if (trimmedLine.startsWith("/*")) {
					comment = true;
					continue;
				} else if (trimmedLine.endsWith("*/")) {
					comment = false;
					continue;
				}
				if (comment) {
					continue;
				}
				if (trimmedLine.endsWith(DEFAULT_DELIMITER)) {
					command.append(line.substring(0, line.lastIndexOf(DEFAULT_DELIMITER)));
					System.out.println(command);

					Statement statement = conn.createStatement();
					statement.execute(command.toString());
					statement.close();

					command.setLength(0);
				} else {
					command.append(line).append(" ");
				}
			}
			// 全部執行成功，添加記錄到migration表
			PreparedStatement ps = conn.prepareStatement("insert into " + migrateTable + "(id) values(?)");
			ps.setLong(1, t);
			ps.executeUpdate();
			ps.close();
			conn.commit();
		} catch (Exception e) {
			conn.rollback();
			throw e;
		}
	}
}
