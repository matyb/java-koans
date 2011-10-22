package com.sandwich.util;
import static com.sandwich.koan.constant.KoanConstants.PERIOD;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sandwich.util.io.directories.DirectoryManager;

public class Strings {
	
	private static final ResourceBundle MESSAGES_BUNDLE;
	// conditionally create messages bundle for proper locale, not on classpath so need to handle manually 
	static{
		ResourceBundle temp = null;
		try {
			temp = new PropertyResourceBundle(new FileInputStream(
				DirectoryManager.injectFileSystemSeparators(
					DirectoryManager.getProjectI18nDir(), 
							new StringBuilder("messages_").append(
									Locale.getDefault().getLanguage()).append(
									".properties").toString())));
		} catch(FileNotFoundException x) {
			try {
				Logger.getAnonymousLogger().log(Level.WARNING, "Your default language is not supported yet. "+x.getLocalizedMessage());
				temp = new PropertyResourceBundle(new FileInputStream(
						DirectoryManager.injectFileSystemSeparators(
							DirectoryManager.getProjectI18nDir(), "messages_en.properties")));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		MESSAGES_BUNDLE = temp;
	}

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
