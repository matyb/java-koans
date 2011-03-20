package com.sandwich.koan.runner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.sandwich.koan.KoanMethod;
import com.sandwich.koan.path.PathToEnlightenment.FileFormatException;

public class KoanComparator implements Comparator<KoanMethod> {
	List<String> orderedKeywords;
	Set<String> methodNamesCompared = new HashSet<String>();
	
	public KoanComparator(String...koans){
		this(Arrays.asList(koans));
	}
	
	public KoanComparator(Collection<String> koans){
		this.orderedKeywords = new ArrayList<String>(koans);
	}
	
	@Override
	public int compare(KoanMethod arg0, KoanMethod arg1) {
		String name0 = arg0.getMethod().getName();
		String name1 = arg1.getMethod().getName();
		int nameScore0 = orderedKeywords.indexOf(name0);
		int nameScore1 = orderedKeywords.indexOf(name1);
		if(-1 == nameScore0){
			error(name0);
		}
		if(-1 == nameScore1){
			error(name1);
		}
		methodNamesCompared.add(name0);
		methodNamesCompared.add(name1);
		return Integer.valueOf(nameScore0).compareTo(nameScore1);
	}

	public void assertXmlAndCodeAreInSynch(){
		for(String methodName : orderedKeywords){
			if(!methodNamesCompared.contains(methodName)){
				throw new FileFormatException(methodName+" was in the PathToEnlightment.xml file - but not present in its suite.");
			}
		}
		for(String methodName : methodNamesCompared){
			if(!orderedKeywords.contains(methodName)){
				throw new FileFormatException(methodName+" was not found in the PathToEnlightment.xml file");
			}
		}
	}
	
	private void error(String name) {
		throw new FileFormatException(name+" is not referenced in PathToEnlightment.xml, its order is far from guarenteed.\n");
	}
}