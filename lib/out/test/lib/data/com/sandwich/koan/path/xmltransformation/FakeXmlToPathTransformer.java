package com.sandwich.koan.path.xmltransformation;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import com.sandwich.koan.path.PathToEnlightenment.Path;

public class FakeXmlToPathTransformer extends XmlToPathTransformerImpl {

	private Map<String, Map<String, KoanElementAttributes>> methodsBySuite;
	
	@SuppressWarnings("unchecked")
	public FakeXmlToPathTransformer() {
		this(Collections.EMPTY_MAP);
	}
	
	public FakeXmlToPathTransformer(Map<String, Map<String, KoanElementAttributes>> methodsBySuite){
		this.methodsBySuite = methodsBySuite;
	}
	
	public Map<String, Map<String, KoanElementAttributes>> getMethodsBySuite() {
		return methodsBySuite;
	}
	
	public void setMethodsBySuite(Map<String, Map<String, KoanElementAttributes>> methodsBySuite) {
		this.methodsBySuite = methodsBySuite;
	}
	
	@Override
	public Path transform(){
		Map<String, Map<String, Map<String, KoanElementAttributes>>> koans = 
			new LinkedHashMap<String, Map<String, Map<String, KoanElementAttributes>>>();
		koans.put("test", new LinkedHashMap<String, Map<String, KoanElementAttributes>>(methodsBySuite));
		return new Path(null,koans);
	}
	
}
