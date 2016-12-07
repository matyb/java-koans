package com.sandwich.koan.path;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.sandwich.koan.ApplicationSettings;
import com.sandwich.koan.Koan;
import com.sandwich.koan.constant.KoanConstants;
import com.sandwich.koan.path.xmltransformation.KoanElementAttributes;
import com.sandwich.koan.path.xmltransformation.XmlToPathTransformer;
import com.sandwich.koan.path.xmltransformation.XmlToPathTransformerImpl;
import com.sandwich.util.io.directories.DirectoryManager;
import com.sandwich.util.io.filecompiler.FileCompiler;

public abstract class PathToEnlightenment {

	static Path theWay;
	static String suiteName;
	static String koanMethod;
	static XmlToPathTransformer xmlToPathTransformer;

	private PathToEnlightenment(){} // non instantiable
	
	static Path createPath(){
		try{
			return getXmlToPathTransformer().transform();
		}catch(Throwable t){
			throw new RuntimeException(t);
		}
	}
	
	static XmlToPathTransformer getXmlToPathTransformer(){
		if(xmlToPathTransformer == null){
			try {
				String filename = DirectoryManager.injectFileSystemSeparators(
						DirectoryManager.getConfigDir(), ApplicationSettings.getPathXmlFileName());
				File file = new File(filename);
				if(!file.exists()){
					throw new RuntimeException("No "+filename+" was found at: "+file.getAbsolutePath());
				}
				xmlToPathTransformer = new XmlToPathTransformerImpl(filename, 
						suiteName, koanMethod);
			} catch (FileNotFoundException e) {
				throw new RuntimeException(e);
			}
		}
		return xmlToPathTransformer;
	}
	
	public static void filterBySuite(String suite){
		suiteName = suite;
		xmlToPathTransformer = null;
	}
	
	public static void filterByKoan(String koan){
		koanMethod = koan;
		xmlToPathTransformer = null;
	}
	
	public static Path getPathToEnlightenment(){
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
	
	public static class Path implements Iterable<Entry<String, Map<String, Map<String, KoanElementAttributes>>>>{
		private Map<String, Map<String, Map<String, KoanElementAttributes>>> koanMethodsBySuiteByPackage;
		private String methodName;
		public Path(){}
		public Path(String methodName, Map<String, Map<String, Map<String, KoanElementAttributes>>> koans) {
			stubKoanMethodsBySuiteByClass(koans);
			this.methodName = methodName;
		}
		public int getTotalNumberOfKoans() {
			if(methodName != null){
				return 1;
			}
			int total = 0;
			Iterator<Entry<String, Map<String, Map<String, KoanElementAttributes>>>> koanMethodsBySuiteByPackageIter = 
				getKoanMethodsBySuiteByPackage();
			while(koanMethodsBySuiteByPackageIter.hasNext()){
				Entry<String, Map<String, Map<String, KoanElementAttributes>>> e = koanMethodsBySuiteByPackageIter.next();
				for(Entry<String, Map<String, KoanElementAttributes>> e1 : e.getValue().entrySet()){
					total += countKoanAnnotationsInJavaFileGivenClassName(e1.getKey());
				}
			}
			return total;
		}
		
		private int countKoanAnnotationsInJavaFileGivenClassName(String className) {
			String[] lines = FileCompiler.getContentsOfJavaFile(
					DirectoryManager.getSourceDir(), className).split(KoanConstants.EOLS);
			String koanClassSimpleNameWithAnnotationPrefix = '@'+Koan.class.getSimpleName();
			int total = 0;
			for(String line : lines){
				String trimmedLine = line.trim();
				if(trimmedLine.contains(koanClassSimpleNameWithAnnotationPrefix)
						&& !trimmedLine.startsWith("//") && !trimmedLine.startsWith("*") && !trimmedLine.startsWith("/*")){
					total++;
				}
			}
			return total;
		}
		
		public Iterator<Entry<String, Map<String, Map<String, KoanElementAttributes>>>> iterator() {
			return getKoanMethodsBySuiteByPackage();
		}
		
		protected Path stubKoanMethodsBySuiteByClass(Map<String, Map<String, Map<String, KoanElementAttributes>>> koanMethodsBySuiteByPackage){
			// unlike other collections in this app, this actually needs to remain mutable after the reference is
			// stored and utilized internally. this is so the DynamicClassLoader can swap out references to 
			// any dynamic classes
			this.koanMethodsBySuiteByPackage = koanMethodsBySuiteByPackage;
			return this;
		}
		protected Iterator<Entry<String, Map<String, Map<String, KoanElementAttributes>>>> getKoanMethodsBySuiteByPackage() {
			return koanMethodsBySuiteByPackage.entrySet().iterator();
		}
		@Override public boolean equals(Object o){
			if(o == this){
				return true;
			}
			if(o instanceof Path){
				if(getKoanMethodsBySuiteByPackage() == ((Path)o).getKoanMethodsBySuiteByPackage()){
					return true;
				}
				if(getKoanMethodsBySuiteByPackage() == null || ((Path)o).getKoanMethodsBySuiteByPackage() == null
						|| getKoanMethodsBySuiteByPackage().getClass() != ((Path)o).getKoanMethodsBySuiteByPackage().getClass()){
					return false;
				}
				Iterator<Entry<String,Map<String, Map<String, KoanElementAttributes>>>> i1 = 
					getKoanMethodsBySuiteByPackage();
				Iterator<Entry<String, Map<String, Map<String, KoanElementAttributes>>>> i2 = 
					((Path)o).getKoanMethodsBySuiteByPackage();
				while(i1.hasNext()){
					Map<String, Map<String, KoanElementAttributes>> m1 = i1.next().getValue();
					Map<String, Map<String, KoanElementAttributes>> m2 = i2.next().getValue();
					if(m1 == m2){
						continue;
					}
					if(			m1 == null 
							||  m2 == null
							||  m1.size() != m2.size()
							||  m1.getClass() != m2.getClass()){
						return false;
					}
					Iterator<Entry<String, Map<String, KoanElementAttributes>>> ii1 = m1.entrySet().iterator();
					Iterator<Entry<String, Map<String, KoanElementAttributes>>> ii2 = m2.entrySet().iterator();
					while(ii1.hasNext()){
						Entry<String, Map<String, KoanElementAttributes>> e1 = ii1.next();
						Entry<String, Map<String, KoanElementAttributes>> e2 = ii2.next();
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
			return getKoanMethodsBySuiteByPackage().hashCode();
		}
		@Override public String toString(){
			return getKoanMethodsBySuiteByPackage().toString();
		}
		public String getOnlyMethodNameToRun() {
			return methodName;
		}
	}
}
