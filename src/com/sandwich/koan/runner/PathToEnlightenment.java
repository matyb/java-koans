package com.sandwich.koan.runner;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sandwich.koan.Koan;
import com.sandwich.koan.KoanConstants;
import com.sandwich.koan.KoanMethod;

public abstract class PathToEnlightenment {

	static Path theWay;

	static Path createPath(){
		try{
			File xml = new File("src/PathToEnlightment.xml");
			if(!xml.exists()){
				throw new FileNotFoundException(xml.getAbsolutePath()
						+ " was not found. it may have been deleted or renamed.");
			}
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(xml);
			doc.getDocumentElement().normalize();
			NodeList nodeLst = doc.getElementsByTagName("Package");
			Map<String, Map<Object, List<KoanMethod>>> koans = new LinkedHashMap<String, Map<Object, List<KoanMethod>>>();
			for(int i = 0; i < nodeLst.getLength(); i++){
				Node node = nodeLst.item(i);
				String packageTitle = node.getAttributes().getNamedItem("name").getNodeValue();
				String pkg = node.getAttributes().getNamedItem("pkg").getNodeValue();
				koans.put(packageTitle, createSuitesAndKoans(pkg, node.getChildNodes()));
			}
			return new Path(koans);
		}catch(Throwable t){
			throw new RuntimeException(t);
		}
	}
	
	private static Map<Object, List<KoanMethod>> createSuitesAndKoans(String pkg, NodeList childNodes) throws DOMException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		Map<Object, List<KoanMethod>> suitesAndKoans = new LinkedHashMap<Object, List<KoanMethod>>();
		for(int i = 0; i < childNodes.getLength(); i++){
			Node node = childNodes.item(i);
			if("Suite".equalsIgnoreCase(node.getNodeName())){
				Class<?> koanSuiteClass = Class.forName(pkg + '.' + node.getAttributes().getNamedItem("class").getNodeValue());
				suitesAndKoans.put(
						koanSuiteClass.newInstance(), Collections.unmodifiableList(
						createKoanMethods(koanSuiteClass, node.getAttributes().getNamedItem("order").getNodeValue())));
			}
		}
		return Collections.unmodifiableMap(suitesAndKoans);
	}
	
	static List<KoanMethod> createKoanMethods(Class<?> koanSuiteClass, String csvOrder) {
		List<KoanMethod> koanMethods = stripNonKoanMethods(koanSuiteClass);
		KoanComparator koanComparator = new KoanComparator(csvOrder);
		Collections.sort(koanMethods, koanComparator);
		assertXmlAndCodeAreInSynch(csvOrder, koanMethods, koanComparator);
		return koanMethods;
	}

	private static void assertXmlAndCodeAreInSynch(String csvOrder,
			List<KoanMethod> koanMethods, KoanComparator koanComparator) {
		if(koanMethods.size() != 1){
			koanComparator.assertXmlAndCodeAreInSynch();
		}else if(!koanMethods.get(0).getMethod().getName().equals(csvOrder)){
			throw new FileFormatException(koanMethods.get(0).getMethod().getName()+
					" was not found in the PathToEnlightment.xml");
		}
	}

	static List<KoanMethod> stripNonKoanMethods(Class<?> koanSuiteClass) {
		List<KoanMethod> koanMethods = new ArrayList<KoanMethod>();
		for(final Method koan : koanSuiteClass.getMethods()){
			final Koan annotation = koan.getAnnotation(Koan.class);
			if(annotation != null){
				koanMethods.add(new KoanMethod(annotation.value(), koan));
			}
		}
		return koanMethods;
	}
	
	public static Path getPathToEnlightment(){
		if(theWay == null){
			theWay = createPath();
		}
		return theWay;
	}
	
	static void removeAllKoanMethodsExcept(Path koans, String koanName) {
		// if more than 1 pkg, or more than 1 suite - something is likely broken before this point
		if(koans.size() != 1 || 
				koans.iterator().next().getValue().size() !=1){
			Logger.getAnonymousLogger().warning("not just one koansuite remains, " +
					"check koan suite name argument - not likely that filtering by method will work.");
		}
		Collection<List<KoanMethod>> values = koans.iterator().next().getValue().values();
		for(List<KoanMethod> methods : values){
			Iterator<KoanMethod> koansListsIter = methods.iterator();
			while(koansListsIter.hasNext()){
				if(!koanName.equalsIgnoreCase(koansListsIter.next().getMethod().getName())){
					koansListsIter.remove();
				}
			}
		}
	}

	static void stagePathToEnlightenment(String pkg, String koanSuite){
		try{
			Class<?> koanClass;
			if(koanSuite.contains(KoanConstants.PERIOD)){
				koanClass = Class.forName(koanSuite);
			}else{
				koanClass = Class.forName(
						new StringBuilder(pkg).append(KoanConstants.PERIOD).append(koanSuite).toString());
			}
			stagePathToEnlightenment(koanClass);
		}catch(Throwable t){
			throw new RuntimeException(t);
		}
	}

	static void stagePathToEnlightenment(String koanSuite){
		try{
			Class<?> koanClass;
			if(koanSuite.contains(KoanConstants.PERIOD)){
				koanClass = Class.forName(koanSuite);
			}else{
				throw new RuntimeException("need package to instantiate the class");
			}
			stagePathToEnlightenment(koanClass);
		}catch(Throwable t){
			throw new RuntimeException(t);
		}
	}
	
	static void stagePathToEnlightenment(Class<?> koanSuite) {
		try {
			stagePathToEnlightenment(koanSuite.newInstance());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	static void stagePathToEnlightenment(Object koanSuite) {
		if(koanSuite instanceof Class<?>){
			stagePathToEnlightenment((Class<?>)koanSuite);
		}
		Map<String, Map<Object, List<KoanMethod>>> koans = new HashMap<String, Map<Object,List<KoanMethod>>>();
		Map<Object, List<KoanMethod>> suiteAndMethods = new HashMap<Object, List<KoanMethod>>();
		List<KoanMethod> methods = PathToEnlightenment.stripNonKoanMethods(koanSuite.getClass());
		suiteAndMethods.put(koanSuite, methods);
		koans.put(koanSuite.toString(), suiteAndMethods);
		PathToEnlightenment.theWay = new Path(koans);
	}
	
	private PathToEnlightenment(){} // non instantiable
	
	public static class FileFormatException extends RuntimeException{
		private static final long serialVersionUID = -1343169944770684376L;
		public FileFormatException(String message){
			super(message);
		}
	}
	
	public static class Path implements Iterable<Entry<String, Map<Object, List<KoanMethod>>>>{
		final Map<String, Map<Object, List<KoanMethod>>> koanMethodsBySuiteByPackage;
		public Path(Map<String, Map<Object, List<KoanMethod>>> koanMethodsBySuiteByPackage){
			this.koanMethodsBySuiteByPackage = Collections.unmodifiableMap(koanMethodsBySuiteByPackage);
		}
		public int getTotalNumberOfKoans() {
			int total = 0;
			for(Entry<String, Map<Object, List<KoanMethod>>> e0 : koanMethodsBySuiteByPackage.entrySet()){
				for(Entry<Object, List<KoanMethod>> e1 : e0.getValue().entrySet()){
					total += e1.getValue().size();
				}
			}
			return total;
		}
		@Override
		public Iterator<Entry<String, Map<Object, List<KoanMethod>>>> iterator() {
			return koanMethodsBySuiteByPackage.entrySet().iterator();
		}
		public int size() {
			return koanMethodsBySuiteByPackage.size();
		}
		public int size(String pkg){
			Map<?,?> suiteAndKoans = koanMethodsBySuiteByPackage.get(pkg);
			if(suiteAndKoans == null){
				return -1;
			}
			return suiteAndKoans.size();
		}
		@Override public boolean equals(Object o){
			if(o == this){
				return true;
			}
			if(o instanceof Path){
				if(koanMethodsBySuiteByPackage == ((Path)o).koanMethodsBySuiteByPackage){
					return true;
				}
				if(koanMethodsBySuiteByPackage == null || ((Path)o).koanMethodsBySuiteByPackage == null
						|| koanMethodsBySuiteByPackage.size() != ((Path)o).koanMethodsBySuiteByPackage.size()
						|| koanMethodsBySuiteByPackage.getClass() != ((Path)o).koanMethodsBySuiteByPackage.getClass()){
					return false;
				}
				Iterator<Entry<String,Map<Object, List<KoanMethod>>>> i1 = 
					koanMethodsBySuiteByPackage.entrySet().iterator();
				Iterator<Entry<String,Map<Object, List<KoanMethod>>>> i2 = 
					((Path)o).koanMethodsBySuiteByPackage.entrySet().iterator();
				while(i1.hasNext()){
					Map<Object, List<KoanMethod>> m1 = i1.next().getValue();
					Map<Object, List<KoanMethod>> m2 = i2.next().getValue();
					if(m1 == m2){
						continue;
					}
					if(			m1 == null 
							||  m2 == null
							||  m1.size() != m2.size()
							||  m1.getClass() != m2.getClass()){
						return false;
					}
					Iterator<Entry<Object, List<KoanMethod>>> ii1 = m1.entrySet().iterator();
					Iterator<Entry<Object, List<KoanMethod>>> ii2 = m2.entrySet().iterator();
					while(ii1.hasNext()){
						Entry<Object, List<KoanMethod>> e1 = ii1.next();
						Entry<Object, List<KoanMethod>> e2 = ii2.next();
						if(!e1.getKey().getClass().equals(e2.getKey().getClass())){
							return false;
						}
						if(!e1.getValue().equals(e2.getValue())){
							return false;
						}
					}
				}
			}
			return true;
		}
		@Override public int hashCode(){
			return koanMethodsBySuiteByPackage.hashCode();
		}
		@Override public String toString(){
			return koanMethodsBySuiteByPackage.toString();
		}
	}
}
