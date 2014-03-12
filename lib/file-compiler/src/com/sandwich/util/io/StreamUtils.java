package com.sandwich.util.io;

import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class StreamUtils {

	public static String convertStreamToString(InputStream stream) {
		try {
			return new Scanner(stream).useDelimiter("\\A").next();
		} catch (NoSuchElementException e) {
			return "";
		}
	}

}
