package com.sandwich.util.io;

import java.io.InputStream;

public class StreamUtils {

	public static String convertStreamToString(InputStream stream) {
		try {
			return new java.util.Scanner(stream).useDelimiter("\\A").next();
		} catch (java.util.NoSuchElementException e) {
			return "";
		}
	}

}
