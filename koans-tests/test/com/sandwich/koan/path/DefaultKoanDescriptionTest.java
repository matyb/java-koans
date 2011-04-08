package com.sandwich.koan.path;

import java.util.List;
import java.util.Map.Entry;

import org.junit.Test;

import com.sandwich.koan.Koan;
import com.sandwich.koan.KoanMethod;
import com.sandwich.koan.constant.KoanConstants;

public class DefaultKoanDescriptionTest {

	@Test
	public void defaultKoanDescriptions() throws Exception {
		StringBuilder exceptionStringBuilder = new StringBuilder(KoanConstants.EOL);
		for (Entry<Object, List<KoanMethod>> suiteAndKoans : 
			PathToEnlightenment.getPathToEnlightment().koanMethodsBySuiteByPackage
				.entrySet().iterator().next().getValue().entrySet()) {
			for(KoanMethod koan : suiteAndKoans.getValue()){
				Koan annotation = koan.getMethod().getAnnotation(Koan.class);
				if (annotation != null && KoanConstants.DEFAULT_KOAN_DESC.equals(koan.getLesson())) {
					exceptionStringBuilder.append(suiteAndKoans.getKey().getClass().getName()).append('.')
							.append(koan.getMethod().getName()).append(KoanConstants.EOL);
				}
			}
		}
		String exceptionString = exceptionStringBuilder.toString();
		if(exceptionString.trim().length() != 0){
			throw new RuntimeException(new StringBuilder(KoanConstants.EOL).append(
				"Following still have default Koan description:").append(exceptionString).toString());
		}
	}
	
}
