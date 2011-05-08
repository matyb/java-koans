package com.sandwich.koan.path.xmltransformation;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.sandwich.koan.KoanMethod;
import com.sandwich.koan.path.PathToEnlightenment.Path;

public class FakeXmlToPathTransformer extends XmlToPathTransformerImpl {

	private Map<Object, List<KoanMethod>> methodsBySuite;
	
	@SuppressWarnings("unchecked")
	public FakeXmlToPathTransformer() {
		this(Collections.EMPTY_MAP);
	}
	
	public FakeXmlToPathTransformer(Map<Object, List<KoanMethod>> methodsBySuite){
		this.methodsBySuite = methodsBySuite;
	}
	
	public Map<Object, List<KoanMethod>> getMethodsBySuite() {
		return methodsBySuite;
	}
	
	public void setMethodsBySuite(Map<Object, List<KoanMethod>> methodsBySuite) {
		this.methodsBySuite = methodsBySuite;
	}
	
	@Override
	public Path transform(){
		Map<String, Map<Object, List<KoanMethod>>> koans = new LinkedHashMap<String, Map<Object, List<KoanMethod>>>();
		koans.put("test", new LinkedHashMap<Object, List<KoanMethod>>(methodsBySuite));
		return new Path(koans);
	}
	
}
