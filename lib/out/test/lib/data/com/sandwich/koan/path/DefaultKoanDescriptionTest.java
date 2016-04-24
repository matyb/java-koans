package com.sandwich.koan.path;

import java.util.Map;
import java.util.Map.Entry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.sandwich.koan.Koan;
import com.sandwich.koan.KoanMethod;
import com.sandwich.koan.constant.KoanConstants;
import com.sandwich.koan.path.xmltransformation.KoanElementAttributes;

public class DefaultKoanDescriptionTest extends CommandLineTestCase {
	
	@Before
	public void setUp(){
		super.setUp();
	}

	@After
	public void tearDown(){
		super.tearDown();
	}
	
	@Test
	public void defaultKoanDescriptions() throws Exception {
		StringBuilder exceptionStringBuilder = new StringBuilder(KoanConstants.EOL);
		for (Entry<String, Map<String, KoanElementAttributes>> suiteAndKoans : 
			PathToEnlightenment.getPathToEnlightenment().getKoanMethodsBySuiteByPackage().next().getValue().entrySet()) {
			for(Entry<String, KoanElementAttributes> koanEntry : suiteAndKoans.getValue().entrySet()){
				KoanMethod koan = KoanMethod.getInstance(koanEntry.getValue());
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
