package com.sandwich.util;
import static com.sandwich.koan.constant.KoanConstants.PERIOD;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Strings {
	
	//$NON-NLS
	private static final ResourceBundle MESSAGES_BUNDLE = ResourceBundle.getBundle("com.sandwich.koan.constant.messages");

	private Strings() {
	}

	public static String getMessage(String key) {
		try {
			return MESSAGES_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
	
	public static String getMessage(Class<?> clazz, String key){
		return Strings.getMessage(new StringBuilder(clazz.getSimpleName()).append(PERIOD).append(key).toString());
	}

	public static String[] getMessages(Class<?> clazz, String key) {
		String[] tmp = getMessage(clazz, key).split(",");
		String[] trimmed = new String[tmp.length];
		for(int i = 0; i < tmp.length; i++){
			trimmed[i] = tmp[i].trim();
		}
		return trimmed;
	}
}
