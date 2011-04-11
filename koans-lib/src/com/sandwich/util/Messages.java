package com.sandwich.util;
import static com.sandwich.koan.constant.KoanConstants.PERIOD;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {
	private static final String BUNDLE_NAME = "com.sandwich.koan.constant.messages"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME);

	private Messages() {
	}

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
	
	public static String getString(Class<?> clazz, String key){
		return Messages.getString(new StringBuilder(clazz.getSimpleName()).append(PERIOD).append(key).toString());
	}

	public static String[] getCSVs(Class<?> clazz, String key) {
		String[] tmp = getString(clazz, key).split(",");
		String[] trimmed = new String[tmp.length];
		for(int i = 0; i < tmp.length; i++){
			trimmed[i] = tmp[i].trim();
		}
		return trimmed;
	}
}
