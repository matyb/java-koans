package com.sandwich.koan.path.xmltransformation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.sandwich.koan.Koan;
import com.sandwich.koan.KoanMethod;
import com.sandwich.koan.path.PathToEnlightenment.FileFormatException;
import com.sandwich.koan.path.PathToEnlightenment.Path;
import com.sandwich.util.KoanComparator;

public class XmlToPathTransformer {

	private final File xmlFile;
	
	public XmlToPathTransformer(String xmlFileLocation) throws FileNotFoundException {
		this.xmlFile = new File(xmlFileLocation);
		if(!xmlFile.exists()){
			throw new FileNotFoundException(xmlFile.getAbsolutePath()
					+ " was not found. it may have been deleted, renamed.");
		}
	}
	
	public Path transform() throws ParserConfigurationException, SAXException,
			IOException, DOMException, ClassNotFoundException,
			InstantiationException, IllegalAccessException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(xmlFile);
		doc.getDocumentElement().normalize();
		NodeList nodeLst = doc.getElementsByTagName("package");
		Map<String, Map<Object, List<KoanMethod>>> koans = new LinkedHashMap<String, Map<Object, List<KoanMethod>>>();
		for(int i = 0; i < nodeLst.getLength(); i++){
			Node node = nodeLst.item(i);
			String packageTitle = node.getAttributes().getNamedItem("name").getNodeValue();
			String pkg = node.getAttributes().getNamedItem("pkg").getNodeValue();
			koans.put(packageTitle, createSuitesAndKoans(pkg, node.getChildNodes()));
		}
		return new Path(koans);
	}
	
	static Map<Object, List<KoanMethod>> createSuitesAndKoans(String pkg, NodeList childNodes) throws DOMException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		Map<Object, List<KoanMethod>> suitesAndKoans = new LinkedHashMap<Object, List<KoanMethod>>();
		for(int i = 0; i < childNodes.getLength(); i++){
			Node node = childNodes.item(i);
			if("suite".equalsIgnoreCase(node.getNodeName())){
				Class<?> koanSuiteClass = Class.forName(pkg + '.' + node.getAttributes().getNamedItem("class").getNodeValue());
				suitesAndKoans.put(
						koanSuiteClass.newInstance(), Collections.unmodifiableList(
						createKoanMethods(koanSuiteClass, node.getChildNodes())));
			}
		}
		return Collections.unmodifiableMap(suitesAndKoans);
	}
	
	static List<KoanMethod> createKoanMethods(Class<?> koanSuiteClass, NodeList nodes) {
		Map<String, KoanElementAttributes> rawKoanLessonByMethodName = extractKoansAndRawLessons(koanSuiteClass.getName(), nodes);
		List<KoanMethod> koanMethods = getKoanMethods(koanSuiteClass, rawKoanLessonByMethodName);
		KoanComparator koanComparator = new KoanComparator(rawKoanLessonByMethodName.keySet());
		Collections.sort(koanMethods, koanComparator);
		assertXmlAndCodeAreInSynch(nodes, koanMethods, koanComparator);
		return koanMethods;
	}

	static Map<String, KoanElementAttributes> extractKoansAndRawLessons(
			String className, NodeList nodes) {
		Map<String, KoanElementAttributes> rawKoanAttributesByMethodName = new LinkedHashMap<String, KoanElementAttributes>();
		for(int i = 0; i < nodes.getLength(); i++){
			Node node = nodes.item(i);
			if("koan".equalsIgnoreCase(node.getNodeName())){
				NamedNodeMap attributes = node.getAttributes();
				String name = attributes.getNamedItem("name").getNodeValue();
				Node lesson = attributes.getNamedItem("lesson");
				String rawLesson = lesson == null ? null : lesson.getNodeValue();
				Node displayKoanIncompleteExceptionNode = attributes.getNamedItem("displayIncompleteKoanException");
				String displayIncompleteKoanException = displayKoanIncompleteExceptionNode == null ?
						null : displayKoanIncompleteExceptionNode.getNodeValue();
				if(rawKoanAttributesByMethodName.containsKey(name)){
					throw new DuplicateKoanException(className, name);
				}
				rawKoanAttributesByMethodName.put(name, new KoanElementAttributes(
						rawLesson, name, displayIncompleteKoanException));
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
	
	static void assertXmlAndCodeAreInSynch(NodeList csvOrder,
			List<KoanMethod> koanMethods, KoanComparator koanComparator) {
		if(koanMethods.size() != 1){
			koanComparator.assertXmlAndCodeAreInSynch();
		} else if (!koanMethods.get(0).getMethod().getName().equals(
				csvOrder.item(1).getAttributes().getNamedItem("name").getNodeValue())) {
			throw new FileFormatException(koanMethods.get(0).getMethod().getName()+
					" was not found in the PathToEnlightment.xml");
		}
	}

	public static List<KoanMethod> getKoanMethods(Class<?> koanSuiteClass, Map<String, KoanElementAttributes> attributesByKoanName) {
		List<KoanMethod> koanMethods = new ArrayList<KoanMethod>();
		for(final Method koan : koanSuiteClass.getMethods()){
			final Koan annotation = koan.getAnnotation(Koan.class);
			if(annotation != null){
				KoanElementAttributes koanMethodAttributes = attributesByKoanName.get(koan.getName());
				String incompleteKoanException = koanMethodAttributes.displayIncompleteKoanException;
				boolean displayIncompleteKoanException = incompleteKoanException == null ||
					"true".equalsIgnoreCase(incompleteKoanException.trim());
				koanMethods.add(new KoanMethod(koanMethodAttributes.lesson, koan, displayIncompleteKoanException));
			}
		}
		return koanMethods;
	}
	
	public static class KoanElementAttributes{
		String lesson, name, displayIncompleteKoanException;
		public KoanElementAttributes(String lesson, String name, String displayIncompleteKoanException){
			this.lesson = lesson;
			this.name = name;
			this.displayIncompleteKoanException = displayIncompleteKoanException;
		}
	}
}

