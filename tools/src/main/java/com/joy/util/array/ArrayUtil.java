package com.joy.util.array;

import java.lang.reflect.Array;

public class ArrayUtil {

	public static boolean isEmpty(Object array) {
		if (array == null) {
			return true;
		}

		Class<?> c = array.getClass();
		if (c.isArray()) {
			return Array.getLength(array) <= 0;
		}
		throw new IllegalArgumentException("must be array");
	}
}
