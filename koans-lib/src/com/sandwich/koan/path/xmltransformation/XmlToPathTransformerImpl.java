package com.sandwich.koan.path.xmltransformation;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sandwich.koan.Koan;
import com.sandwich.koan.KoanMethod;
import com.sandwich.koan.path.PathToEnlightenment.Path;
import com.sandwich.util.io.DynamicClassLoader;

public class XmlToPathTransformerImpl implements XmlToPathTransformer {

	private final File xmlFile;
	private final String suiteName;
	private final String methodName;

	public XmlToPathTransformerImpl(){
		xmlFile = null;
		suiteName = null;
		methodName = null;
	}

	public XmlToPathTransformerImpl(String xmlFileLocation, String suiteName, String methodName) throws FileNotFoundException {
		this.xmlFile = new File(xmlFileLocation);
		this.suiteName = suiteName;
		this.methodName = methodName;
		if(!xmlFile.exists()){
		throw new FileNotFoundException(xmlFile.getAbsolutePath()
				+ " was not found. it may have been deleted, renamed.");
		}
	}
	
	public Path transform(){
		try{
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(xmlFile);
			doc.getDocumentElement().normalize();
			NodeList nodeLst = doc.getElementsByTagName("package");
			/*	
			 * Map of string values read from xml in following form
			 * package
			 * 	  |-suite class name
			 * 		      |-method name
			 * 				     |-method attributes from xml
			 */
			Map<String, Map<String, Map<String, KoanElementAttributes>>> koans = 
				new LinkedHashMap<String, Map<String, Map<String, KoanElementAttributes>>>();
			for(int i = 0; i < nodeLst.getLength(); i++){
				Node node = nodeLst.item(i);
				String packageTitle = node.getAttributes().getNamedItem("name").getNodeValue();
				String pkg = node.getAttributes().getNamedItem("pkg").getNodeValue();
				koans.put(packageTitle, getKoanElementAttributesByMethodNameBySuite(pkg, node.getChildNodes()));
			}
			return new Path().stubKoanMethodsBySuiteByClass(koans);
		}catch(Exception x){
			throw new RuntimeException(x);
		}
	}
	
	Map<String, Map<String, KoanElementAttributes>> getKoanElementAttributesByMethodNameBySuite(String pkg,
			NodeList childNodes) throws DOMException, ClassNotFoundException,
			InstantiationException, IllegalAccessException {
		Map<String, Map<String, KoanElementAttributes>> koansByMethodNameByClass = 
			new LinkedHashMap<String, Map<String, KoanElementAttributes>>();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node node = childNodes.item(i);
			if ("suite".equalsIgnoreCase(node.getNodeName())) {
				String className = pkg
						+ '.'
						+ node.getAttributes().getNamedItem("class")
								.getNodeValue();
				if (suiteName == null || suiteName.equalsIgnoreCase(className)) {
					koansByMethodNameByClass.put(className, extractKoansAndRawLessons(className, node.getChildNodes()));
				}
			}
		}
		return koansByMethodNameByClass;
	}

//	List<KoanMethod> createKoanMethods(Class<?> koanSuiteClass, NodeList nodes) {
//		Map<String, KoanElementAttributes> rawKoanLessonByMethodName = extractKoansAndRawLessons(
//				koanSuiteClass.getName(), nodes);
//		List<KoanMethod> koanMethods = getKoanMethods(koanSuiteClass,
//				rawKoanLessonByMethodName);
//		KoanComparator koanComparator = new KoanComparator(
//				rawKoanLessonByMethodName.keySet());
//		Collections.sort(koanMethods, koanComparator);
//		return koanMethods;
//	}

	Map<String, KoanElementAttributes> extractKoansAndRawLessons(
			String className, NodeList nodes) {
		Map<String, KoanElementAttributes> rawKoanAttributesByMethodName = new LinkedHashMap<String, KoanElementAttributes>();
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			if ("koan".equalsIgnoreCase(node.getNodeName())) {
				NamedNodeMap attributes = node.getAttributes();
				String name = attributes.getNamedItem("name").getNodeValue();
				Node lesson = attributes.getNamedItem("lesson");
				String rawLesson = lesson == null ? null : lesson
						.getNodeValue();
				Node displayKoanIncompleteExceptionNode = attributes
						.getNamedItem("displayIncompleteKoanException");
				String displayIncompleteKoanException = displayKoanIncompleteExceptionNode == null ? null
						: displayKoanIncompleteExceptionNode.getNodeValue();
				if (rawKoanAttributesByMethodName.containsKey(name)) {
					throw new DuplicateKoanException(className, name);
				}
				rawKoanAttributesByMethodName.put(name,
						new KoanElementAttributes(rawLesson, name,
								displayIncompleteKoanException, className));
			}
		}
		return rawKoanAttributesByMethodName;
	}

		static class DuplicateKoanException extends RuntimeException {
			private static final long serialVersionUID = 3846796580641690961L;
			public DuplicateKoanException(String className, String name){
				super("Duplicate koans in the same suite: "+className+" with the name "+name);
			}
		}

		public List<KoanMethod> getKoanMethods(String koanSuiteClass, Map<String, KoanElementAttributes> attributesByKoanName) {
			List<KoanMethod> koanMethods = new ArrayList<KoanMethod>();
			DynamicClassLoader loader = new DynamicClassLoader();
			for(final Method koan : loader.loadClass(koanSuiteClass).getMethods()){
				final Koan annotation = koan.getAnnotation(Koan.class);
				if(annotation != null){
					if(methodName == null || methodName.equalsIgnoreCase(koan.getName())){
						KoanElementAttributes koanMethodAttributes = attributesByKoanName.get(koan.getName());
						koanMethodAttributes = koanMethodAttributes == null ? 
									new KoanElementAttributes(null, koan.getName(), null, koanSuiteClass) : koanMethodAttributes;
						koanMethods.add(KoanMethod.getInstance(koanMethodAttributes));
					}
				}
			}
			return koanMethods;
		}

}

