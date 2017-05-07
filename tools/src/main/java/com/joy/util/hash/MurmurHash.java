package com.joy.util.hash;

/**
 * MurmurHash算法
 * 
 * @author caosi
 *
 */
public class MurmurHash {

	private static byte[] toBytesWithoutEncoding(String str) {
		int len = str.length();
		int pos = 0;
		byte[] buf = new byte[len << 1];
		for (int i = 0; i < len; i++) {
			char c = str.charAt(i);
			buf[pos++] = (byte) (c & 0xFF);
			buf[pos++] = (byte) (c >> 8);
		}
		return buf;
	}

	public static int hash32(String str) {
		byte[] bytes = toBytesWithoutEncoding(str);
		return hash32(bytes, bytes.length);
	}

	public static int hash32(final byte[] data, int length) {
		return hash32(data, length, 0x9747b28c);
	}

	public static int hash32(final byte[] data, int length, int seed) {
		final int m = 0x5bd1e995;
		final int r = 24;

		int h = seed ^ length;
		int length4 = length / 4;

		for (int i = 0; i < length4; i++) {
			int i4 = i * 4;
			int k = (data[i4 + 0] & 0xff) + ((data[i4 + 1] & 0xff) << 8) + ((data[i4 + 2] & 0xff) << 16)
					+ ((data[i4 + 3] & 0xff) << 24);
			k *= m;
			k ^= k >>> r;
			k *= m;
			h *= m;
			h ^= k;
		}
		switch (length % 4) {
		case 3:
			h ^= (data[(length & ~3) + 2] & 0xff) << 16;
		case 2:
			h ^= (data[(length & ~3) + 2] & 0xff) << 8;
		case 1:
			h ^= (data[(length & ~3) + 2] & 0xff);
			h *= m;
		}
		h ^= h >>> 13;
		h *= m;
		h ^= h >>> 15;
		return h;
	}

	public static long hash64(String str) {
		byte[] bytes = toBytesWithoutEncoding(str);
		return hash64(bytes, bytes.length);
	}

	public static long hash64(final byte[] data, int length) {
		return hash64(data, length, 0xe17a1465);
	}

	public static long hash64(final byte[] data, int length, int seed) {
		final long m = 0xc6a4a7935bd1e995L;
		final int r = 47;

		long h = (seed & 0xffffffffl) ^ (length * m);
		int length8 = length / 8;

		for (int i = 0; i < length8; i++) {
			int i8 = i * 8;
			long k = ((long) data[i8 + 0] & 0xff) + (((long) data[i8 + 1] & 0xff) << 8)
					+ (((long) data[i8 + 2] & 0xff) << 16) + (((long) data[i8 + 3] & 0xff) << 24)
					+ (((long) data[i8 + 4] & 0xff) << 32) + (((long) data[i8 + 5] & 0xff) << 40)
					+ (((long) data[i8 + 6] & 0xff) << 48) + (((long) data[i8 + 7] & 0xff) << 56);
			k *= m;
			k ^= k >>> r;
			k *= m;
			h ^= k;
			h *= m;
		}
		switch (length % 8) {
		case 7:
			h ^= (data[(length & ~7) + 6] & 0xff) << 48;
		case 6:
			h ^= (data[(length & ~7) + 5] & 0xff) << 40;
		case 5:
			h ^= (data[(length & ~7) + 4] & 0xff) << 32;
		case 4:
			h ^= (data[(length & ~7) + 3] & 0xff) << 24;
		case 3:
			h ^= (data[(length & ~7) + 2] & 0xff) << 16;
		case 2:
			h ^= (data[(length & ~7) + 1] & 0xff) << 8;
		case 1:
			h ^= (data[(length & ~7)] & 0xff);
			h *= m;
		}
		h ^= h >>> r;
		h *= m;
		h ^= h >>> r;
		return h;

	}
	

}
