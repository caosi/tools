package com.joy.util.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.joy.util.string.StringUtil;

public class ExcelUtil {

	public static final String XLS = "xls";
	public static final String XLSX = "xlsx";

	// 数字
	public static final String COL_NUM = "N";
	// boolean true<=>false
	public static final String COL_BOOLEAN = "B";
	// 字符串
	public static final String COL_STRING = "S";
	// 数组 不能单独使用，只能配合 N B S使用
	public static final String COL_ARRAY = "A";
	// 注释，可忽略字段,只能单独使用
	public static final String COL_COMMENT = "O";
	// 数组字段，分隔符
	public static final String ARRAY_SPLIT = ";";

	////////////////////////////////////////////////
	// 列A 列B 列C
	// 列A1 列B1 列C1
	// N B S
	// 1 true hello
	///////////////////////////////////////////////

	public static List<Map<String, Object>> read(String filePath) throws FileNotFoundException, IOException {
		File file = new File(filePath);
		if (!file.exists()) {
			throw new IllegalArgumentException("file [" + filePath + "] not exist.");
		}
		String name = file.getName();
		int dotIndex = name.lastIndexOf('.');
		if (dotIndex < 0) {
			throw new IllegalArgumentException("file [" + filePath + "] has no extension.");
		}
		String extension = name.substring(dotIndex + 1);
		try (FileInputStream fis = new FileInputStream(file)) {
			switch (extension) {
			case XLS:
				return readXls(fis);
			case XLSX:
				return readXlsx(fis);
			default:
				throw new IllegalArgumentException("file[" + filePath + "] not vaild excel file.");
			}
		}
	}

	private static List<Map<String, Object>> readXlsx(FileInputStream fis) throws IOException {
		List<Map<String, Object>> datas = new ArrayList<>();
		try (XSSFWorkbook workBook = new XSSFWorkbook(fis)) {
			XSSFSheet sheet = workBook.getSheetAt(0);
			int rows = sheet.getPhysicalNumberOfRows();
			Map<Integer, String> types = new HashMap<Integer, String>();
			Map<Integer, String> names = new HashMap<Integer, String>();
			// 第一行表头
			// 第二行字段名称
			XSSFRow colNames = sheet.getRow(1);
			int cells = colNames.getPhysicalNumberOfCells();
			for (int j = 0; j < cells; j++) {
				XSSFCell cell = colNames.getCell(j);
				String cellValue = (String) getCellValue(cell, COL_STRING);
				names.put(j, cellValue);
			}
			// 第三行列类型
			XSSFRow typeRow = sheet.getRow(2);
			// 获取到Excel文件中的所有的列类型
			for (int j = 0; j < cells; j++) {
				XSSFCell cell = typeRow.getCell(j);
				String cellValue = (String) getCellValue(cell, COL_STRING);
				types.put(j, cellValue);
			}

			// 遍历行­（从第三行开始）
			for (int i = 3; i < rows; i++) {
				// 读取左上端单元格(从第二行开始)
				XSSFRow row = sheet.getRow(i);
				// 准备当前行 所储存值的map
				Map<String, Object> val = new HashMap<String, Object>();
				// 遍历列
				for (int j = 0; j < cells; j++) {
					if (StringUtil.isEqual(types.get(j), COL_COMMENT)) {
						continue;
					}
					// 获取到列的值­
					XSSFCell cell = row.getCell(j);
					if (cell == null) {
						continue;
					}
					Object cellValue = getCellValue(cell, types.get(j));
					if (cellValue == null) {
						continue;
					}
					val.put(names.get(j), cellValue);
				}
				datas.add(val);
			}
		}
		return datas;
	}

	private static List<Map<String, Object>> readXls(FileInputStream fis) throws IOException {
		List<Map<String, Object>> datas = new ArrayList<>();
		try (HSSFWorkbook workBook = new HSSFWorkbook(fis)) {
			HSSFSheet sheet = workBook.getSheetAt(0);
			int rows = sheet.getPhysicalNumberOfRows();
			Map<Integer, String> types = new HashMap<Integer, String>();
			Map<Integer, String> names = new HashMap<Integer, String>();
			// 第一行表头
			// 第二行字段名称
			HSSFRow colNames = sheet.getRow(1);
			int cells = colNames.getPhysicalNumberOfCells();
			for (int j = 0; j < cells; j++) {
				HSSFCell cell = colNames.getCell(j);
				String cellValue = (String) getCellValue(cell, COL_STRING);
				names.put(j, cellValue);
			}
			// 第三行列类型
			HSSFRow typeRow = sheet.getRow(2);
			// 获取到Excel文件中的所有的列类型
			for (int j = 0; j < cells; j++) {
				HSSFCell cell = typeRow.getCell(j);
				String cellValue = (String) getCellValue(cell, COL_STRING);
				types.put(j, cellValue);
			}

			// 遍历行­（从第三行开始）
			for (int i = 3; i < rows; i++) {
				// 读取左上端单元格(从第二行开始)
				HSSFRow row = sheet.getRow(i);
				// 准备当前行 所储存值的map
				Map<String, Object> val = new HashMap<String, Object>();
				// 遍历列
				for (int j = 0; j < cells; j++) {
					if (StringUtil.isEqual(types.get(j), COL_COMMENT)) {
						continue;
					}
					// 获取到列的值­
					HSSFCell cell = row.getCell(j);
					if (cell == null) {
						continue;
					}
					Object cellValue = getCellValue(cell, types.get(j));
					if (cellValue == null) {
						continue;
					}
					val.put(names.get(j), cellValue);
				}
				datas.add(val);
			}
		}
		return datas;
	}

	private static Object getCellValue(Cell cell, String type) {
		@SuppressWarnings("deprecation")
		CellType cellType = cell.getCellTypeEnum();

		String cellContent = null;
		switch (cellType) {
		case BLANK:
			return null;
		case BOOLEAN:
			cellContent = String.valueOf(cell.getBooleanCellValue());
			break;
		case FORMULA:
			cellContent = String.valueOf(cell.getCellFormula());
			break;
		case NUMERIC:
			cellContent = String.valueOf(cell.getNumericCellValue());
			break;
		case ERROR:
			cellContent = String.valueOf(cell.getErrorCellValue());
			break;
		case STRING:
			cellContent = cell.getStringCellValue();
			break;
		default:
			break;
		}
		if (StringUtil.isEmpty(cellContent)) {
			return null;
		}
		cellContent = cellContent.trim();
		if (type.contains(COL_NUM)) {
			if (type.contains(COL_ARRAY)) {
				String[] array = cellContent.split(ARRAY_SPLIT);
				List<Number> nums = new ArrayList<>();
				for (String e : array) {
					if (StringUtil.isEmpty(e)) {
						continue;
					}
					nums.add(toNumber(e));
				}
				return nums;
			}
			return toNumber(cellContent);
		} else if (type.contains(COL_BOOLEAN)) {
			if (type.contains(COL_ARRAY)) {
				String[] array = cellContent.split(ARRAY_SPLIT);
				List<Boolean> bs = new ArrayList<>();
				for (String e : array) {
					if (StringUtil.isEmpty(e)) {
						continue;
					}
					Boolean b = Boolean.valueOf(e);
					bs.add(b);
				}
				return bs;
			}
			return Boolean.valueOf(cellContent);
		} else if (type.contains(COL_STRING)) {
			if (type.contains(COL_ARRAY)) {
				String[] array = cellContent.split(ARRAY_SPLIT);
				List<String> ss = new ArrayList<>();
				for (String e : array) {
					if (StringUtil.isEmpty(e)) {
						continue;
					}
					ss.add(e);
				}
				return ss;
			}
			return cellContent;
		}
		return null;
	}

	private static Number toNumber(String e) {
		if (e.indexOf('.') > 0) {
			return Double.valueOf(e);
		} else {
			try {
				return Integer.valueOf(e);
			} catch (Exception e1) {
				try {
					return Long.valueOf(e);
				} catch (Exception e2) {
					throw new IllegalArgumentException("无法解析的数字:" + e);
				}
			}
		}

	}
}
