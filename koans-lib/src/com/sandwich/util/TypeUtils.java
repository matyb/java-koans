package com.sandwich.util;

public class TypeUtils {

	public static Class<?> getType(int value) {
		return int.class;
	}

	public static Class<?> getType(long value) {
		return long.class;
	}

	public static Class<?> getType(float value) {
		return float.class;
	}

	public static Class<?> getType(double value) {
		return double.class;
	}

	public static Class<?> getType(byte value) {
		return byte.class;
	}

	public static Class<?> getType(char value) {
		return char.class;
	}

	public static Class<?> getType(short value) {
		return short.class;
	}

	public static Class<?> getType(Object value) {
		return value.getClass();
	}

}
