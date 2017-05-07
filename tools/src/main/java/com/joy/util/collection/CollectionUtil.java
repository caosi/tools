package com.joy.util.collection;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class CollectionUtil {

	public static boolean isEmpty(Object obj) {
		if (obj == null) {
			return true;
		}

		Class<?> c = obj.getClass();
		if (Collection.class.isAssignableFrom(c)) {
			return ((Collection<?>) obj).isEmpty();
		}
		if (Map.class.isAssignableFrom(c)) {
			return ((Map<?, ?>) obj).isEmpty();
		}
		throw new IllegalArgumentException("must implement Collection or Map");

	}

	/**
	 * 插入排序 插入排序比较适合用于“少量元素的数组”。
	 */
	public static <T> void insertSort(List<T> list, Comparator<T> comp) {
		int size = list.size();
		for (int index = 1; index < size; index++) {
			T t = list.get(index);
			int index1 = index - 1;
			while (index1 >= 0 && comp.compare(list.get(index1), t) > 0) {
				list.set(index1 + 1, list.get(index1));
				index1 -= 1;
			}
			list.set(index1 + 1, t);
		}
	}
}
