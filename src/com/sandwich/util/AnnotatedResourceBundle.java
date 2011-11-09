package com.sandwich.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Scanner;

public class AnnotatedResourceBundle {

	/**
	 * end of a properties file name
	 */
	private static final String FILE_SUFFIX = ".properties";
	/**
	 * start of an annotated properties annotation line
	 */
	private static final String ANNOTATION_LINE_START = "#@";
	/**
	 * key & value separator in standard properties files
	 */
	private static final String PROP_KEY_VAL_SEPARATOR = "=";
	/**
	 * key & value separator in property annotations
	 */
	private static final String KEY_VALUE_SEPARATOR = ":";
	/**
	 * annotated key:value pair delimiter
	 */
	private static final String ANNOTATION_VALUE_DELIMITER = ";";
	/**
	 * start of a variable
	 */
	private static final String EMBEDDED_VALUE_START = "${";
	/**
	 * end of a variable
	 */
	private static final String EMBEDDED_VALUE_END = "}";
	
	private ResourceBundle bundle;
	private final String bundleName;
	private final ClassLoader classLoaderWithProps;
	private final Map<String, Map<String, String>> propertyAttributes;
	
	/** 
	 * uninitialized instance, for testing (may not be initialized outside construction easily)
	 */
	AnnotatedResourceBundle(){
		bundle = null;
		bundleName = null;
		classLoaderWithProps = null;
		propertyAttributes = Collections.emptyMap();
	}
	
	/**
	 * initialized instance of bundle w/ check for existing resource
	 */
	AnnotatedResourceBundle(String bundleName, ClassLoader classLoaderWithProps){
		this.bundleName = bundleName;
		ResourceBundle bundle = createBundle();
		this.bundle = bundle;
		this.classLoaderWithProps = classLoaderWithProps;
		propertyAttributes = readPropertyFileForAnnotations();
	}

	/**
	 * creates a new resourcebundle from this instances construction arguments
	 * @return a newly constructed java.util.ResourceBundle
	 */
	private ResourceBundle createBundle() {
		ResourceBundle bundle = ResourceBundle.getBundle(bundleName.endsWith(FILE_SUFFIX) ? 
				bundleName.substring(0, bundleName.length() - FILE_SUFFIX.length()) : bundleName);
		if(bundle == null){
			throw new IllegalArgumentException("resource bundle may not be null.");
		}
		return bundle;
	}
	
	/**
	 * read all properties from rb
	 * @return
	 */
	Map<String, Map<String, String>> readPropertyFileForAnnotations(){
		return readPropertyFileForAnnotations(bundle.getKeys());
	}
	
	private Map<String, Map<String, String>> readPropertyFileForAnnotations(Enumeration<String> keysEnumeration){
		return readPropertyFileForAnnotations(Collections.list(keysEnumeration));
	}
	
	/**
	 * read a specific list of keys from rb (namely for testing)
	 * @param keysEnumeration
	 * @return
	 */
	private Map<String, Map<String, String>> readPropertyFileForAnnotations(List<String> keys) {
		Map<String, Map<String, String>> propertyAttributes = new LinkedHashMap<String, Map<String, String>>();
		File propertiesFile = findResourceBundleFile();
		String previousLine = null;
		try {
			Scanner scanner = new Scanner(propertiesFile);
			while(scanner.hasNext()){
				String line = scanner.nextLine();
				int indexOfEquals = line.indexOf(PROP_KEY_VAL_SEPARATOR); //property line
				if(indexOfEquals >= 0) {
					String lKey = line.substring(0, indexOfEquals);
					Map<String, String> attributes = Collections.emptyMap();
					for(final String key : keys){
						if(lKey.equals(key) && previousLine != null){
							attributes = parseAttributesFromLine(previousLine);
						}
					}
					propertyAttributes.put(lKey, attributes);
				}
				previousLine = line;
			}
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
		return propertyAttributes;
	}

	/**
	 * constructs a map of key:value pairs from annotated line, or empty map if
	 * not an annotated line. takes a proper annotation line, not a key for an
	 * annotated property.
	 * 
	 * @param line
	 * @return
	 */
	Map<String, String> parseAttributesFromLine(String line) {
		if(line == null || !line.startsWith(ANNOTATION_LINE_START)){
			return Collections.emptyMap();
		}
		line = line.substring(2).trim();
		Map<String, String> attrs = new LinkedHashMap<String, String>();
		String[] segments = line.split(ANNOTATION_VALUE_DELIMITER);
		for(String segment : segments){
			int indexOf = segment.indexOf(KEY_VALUE_SEPARATOR); 
			if(indexOf >= 0){
				String value = segment.substring(indexOf + 1, segment.length());
				value = insertOtherPropertyValues(value);
				attrs.put(segment.substring(0, indexOf).trim(), value);
			}
		}
		return attrs;
	}

	/**
	 * insert dynamic variables if start/end values are valid and present. otherwise, return the passed in string.
	 * @param value
	 * @return
	 */
	private String insertOtherPropertyValues(String value) {
		int start = value.indexOf(EMBEDDED_VALUE_START);
		int end = value.indexOf(EMBEDDED_VALUE_END);
		while(end > start && end > -1 && start > -1){
			String key = value.substring(start + EMBEDDED_VALUE_START.length(), end);
			String rawKey = new StringBuilder(EMBEDDED_VALUE_START).append(key).append(EMBEDDED_VALUE_END).toString();
			String replacement = bundle.getString(key);
			value = value.replace(rawKey, replacement);
			start = value.indexOf(EMBEDDED_VALUE_START);
			end = value.indexOf(EMBEDDED_VALUE_END);
		}
		return value;
	}

	/**
	 * get a file instance from the same argument to get a resource bundle.
	 */
	File findResourceBundleFile() {
		URL url = classLoaderWithProps.getResource(bundleName.endsWith(FILE_SUFFIX) ?
				bundleName : bundleName + FILE_SUFFIX);
		try {
			return new File(url.toURI());
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * access this instance's bundle directly.
	 * @return
	 */
	public ResourceBundle getBundle() {
		return bundle;
	}
	
	/**
	 * wraps access to bundle.getString(key); a convenience method.
	 * @param key
	 * @return
	 */
	public String getString(String key){
		return getBundle().getString(key);
	}
	
	/**
	 * return a map of annotated key/value pairs for the passed in property key.
	 * an empty map if no annotated is present for key specified.
	 * 
	 * @param key
	 * @return
	 */
	public Map<String, String> getAttributes(String key){
		return propertyAttributes.get(key);
	}
	
	/**
	 * forces file to be reread for properties and annotations
	 */
	public void refreshCache(){
		propertyAttributes.clear();
		ResourceBundle.clearCache(classLoaderWithProps);
		bundle = createBundle();
		propertyAttributes.putAll(readPropertyFileForAnnotations());
		
	}
	
	/**
	 * public access to construction of bundle instance. merely passes through
	 * to pkg private constructor currently, utilized to simplify conversion to
	 * cachine if it is ever necessary.
	 * 
	 * @param bundleName
	 * @param classLoader
	 * @return
	 */
	public static AnnotatedResourceBundle getBundle(String bundleName, ClassLoader classLoader){
		return new AnnotatedResourceBundle(bundleName, classLoader);
	}

}
