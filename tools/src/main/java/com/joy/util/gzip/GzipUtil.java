package com.joy.util.gzip;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GzipUtil {
	
	private GzipUtil(){}

	public static byte[] compress(String str){

		// 创建一个新的 byte 数组输出流
		try (ByteArrayOutputStream out = new ByteArrayOutputStream();
		// 使用默认缓冲区大小创建新的输出流
				GZIPOutputStream gzip = new GZIPOutputStream(out)) {
			// 将 b.length 个字节写入此输出流
			gzip.write(str.getBytes("UTF-8"));
			gzip.finish();
			gzip.flush();
			out.flush();
			return out.toByteArray();
		}catch(IOException e){
			e.printStackTrace();
		}
		return null;
	}

	public static String unCompress(byte[] bytes){

		// 创建一个新的 byte 数组输出流
		try (ByteArrayOutputStream out = new ByteArrayOutputStream();
		// 创建一个 ByteArrayInputStream，使用 buf 作为其缓冲区数组
				ByteArrayInputStream in = new ByteArrayInputStream(bytes);
				// 使用默认缓冲区大小创建新的输入流
				GZIPInputStream gzip = new GZIPInputStream(in)) {
			byte[] buffer = new byte[4096];
			int n = 0;
			while ((n = gzip.read(buffer)) > 0) {// 将未压缩数据读入字节数组
				// 将指定 byte 数组中从偏移量 off 开始的 len 个字节写入此 byte数组输出流
				out.write(buffer, 0, n);
			}
			// 使用指定的 charsetName，通过解码字节将缓冲区内容转换为字符串
			return out.toString("UTF-8");
		}catch(IOException e){
			e.printStackTrace();
		}
		return null;
	}
}
