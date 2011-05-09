package com.sandwich.koan.path;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.sandwich.koan.KoanMethod;
import com.sandwich.koan.constant.KoanConstants;
import com.sandwich.koan.path.xmltransformation.XmlToPathTransformer;

public abstract class PathToEnlightenment {

	static Path theWay;
	static String suiteName;
	static String koanMethod;

	private PathToEnlightenment(){} // non instantiable
	
	static Path createPath(){
		try{
			return new XmlToPathTransformer(KoanConstants.PATH_XML_LOCATION, suiteName, koanMethod)
				.transform();
		}catch(Throwable t){
			throw new RuntimeException(t);
		}
	}
	
	public static void filterBySuite(String suite){
		suiteName = suite;
	}
	
	public static void filterByKoan(String koan){
		koanMethod = koan;
	}
	
	public static Path getPathToEnlightment(){
		if(theWay == null){
			theWay = createPath();
		}
		return theWay;
	}
	
	public static class FileFormatException extends RuntimeException{
		private static final long serialVersionUID = -1343169944770684376L;
		public FileFormatException(String message){
			super(message);
		}
	}
	
	public static class Path implements Iterable<Entry<String, Map<Object, List<KoanMethod>>>>{
		final Map<String, Map<Object, List<KoanMethod>>> koanMethodsBySuiteByPackage;
		public Path(Map<String, Map<Object, List<KoanMethod>>> koanMethodsBySuiteByPackage){
			this.koanMethodsBySuiteByPackage = koanMethodsBySuiteByPackage;
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

	public static void replace(Class<?> cls) {
		for(Entry<String,Map<Object, List<KoanMethod>>> e : getPathToEnlightment().koanMethodsBySuiteByPackage.entrySet()){
			Map<Object, List<KoanMethod>> methodsBySuite = e.getValue();
			Map<Object, List<KoanMethod>> replacementValue = new LinkedHashMap<Object, List<KoanMethod>>();
			for(Entry<Object, List<KoanMethod>> e1 : methodsBySuite.entrySet()){
				Object suite = e1.getKey();
				if(suite.getClass().getName().equals(cls.getName())){
					try {
						replacementValue.put(cls.newInstance(), copyMethods(cls, e1.getValue()));
					} catch (Exception e2) {
						e2.printStackTrace();
					} 
				}else{
					replacementValue.put(suite, e1.getValue());
				}
			}
			e.setValue(replacementValue);
		}
	}

	private static List<KoanMethod> copyMethods(Class<?> cls, List<KoanMethod> methodsBySuite) {
		List<KoanMethod> newMethods = new ArrayList<KoanMethod>();
		try {
			Map<String, Method> methodByName = new HashMap<String, Method>();
			for(Method method : cls.getDeclaredMethods()){
				methodByName.put(method.getName(), method);
			}
			for(KoanMethod method : methodsBySuite){
				newMethods.add(method.clone(methodByName.get(method.getMethod().getName())));
			}
		} catch (Exception e2) {
			throw new RuntimeException(e2);
		}
		return newMethods;
	}
}
