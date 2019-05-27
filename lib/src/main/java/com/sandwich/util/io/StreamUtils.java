package com.sandwich.util.io;

import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class StreamUtils {

	public static String convertStreamToString(InputStream stream) {
		Scanner scanner = new Scanner(stream);
		try {
			return scanner.useDelimiter("\\A").next();
		} catch (NoSuchElementException e) {
			return "";
		} finally {
			scanner.close();
		}
	}

}
