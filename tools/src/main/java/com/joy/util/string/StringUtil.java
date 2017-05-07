package com.joy.util.string;

public class StringUtil {

	public static String toString(char[] charArray) {
		return new String(charArray);
	}

	public static boolean isEmpty(String str) {
		return str == null || str.isEmpty();
	}

	public static boolean isEqual(String str, String str1) {
		if (str == null || str1 == null) {
			return false;
		}
		return str.equals(str1);
	}

	public static boolean isEqualsIgnoreCase(String str, String str1) {
		if (str == null || str1 == null) {
			return false;
		}
		return str.equalsIgnoreCase(str1);
	}

}
