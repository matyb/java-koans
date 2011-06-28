package com.sandwich.koan.path;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.sandwich.koan.path.xmltransformation.XmlVariableInjector;
import com.sandwich.koan.suite.OneFailingKoan;

public class XmlVariableInjectorTest {

	@Test
	public void construction_nullMethod() throws Exception {
		try{
			new XmlVariableInjector("", null);
			fail("why construct w/ null method?");
		}catch(IllegalArgumentException t){
			// this is ok - we want this!
		}
	}
	
//	@Test
//	public void construction_nullLesson() throws Exception {
//		try{
//			new XmlVariableInjector(null, Object.class.getDeclaredMethod("equals", Object.class));
//			fail("why construct w/ null lesson?");
//		}catch(IllegalArgumentException t){
//			// this is ok - we want this!
//		}
//	}
	
	@Test
	public void injectInputVariables_filePath() throws Exception {
		String lesson = "meh ${file_path}";
		String result = new XmlVariableInjector(lesson, OneFailingKoan.class.getDeclaredMethod("koanMethod"))
			.injectLessonVariables();
		String firstPkgName = "com";
		// just inspect anything beyond the root of the project
		result = result.substring(result.indexOf(firstPkgName)+firstPkgName.length(), result.length());
		assertTrue(result.indexOf(firstPkgName) < result.indexOf("sandwich"));
		assertTrue(result.indexOf("sandwich") < result.indexOf("koan"));
		assertTrue(result.indexOf("koan") < result.indexOf("suite"));
	}
	
	@Test
	public void injectInputVariables_fileName() throws Exception {
		String lesson = "meh ${file_name}";
		String result = new XmlVariableInjector(lesson, OneFailingKoan.class.getDeclaredMethod("koanMethod"))
			.injectLessonVariables();
		assertEquals("meh OneFailingKoan", result);
	}
	
	@Test
	public void injectInputVariables_methodName() throws Exception {
		String lesson = "meh ${method_name}";
		String result = new XmlVariableInjector(lesson, OneFailingKoan.class.getDeclaredMethod("koanMethod"))
			.injectLessonVariables();
		assertEquals("meh koanMethod", result);
	}
	
	@Test
	public void nothingToInject() throws Exception {
		String lesson = " meh ea asdwdw s ";
		assertSame(lesson, new XmlVariableInjector(lesson, 
			OneFailingKoan.class.getDeclaredMethod("koanMethod")).injectLessonVariables());
	}
}
