package com.sandwich.util.io;

import org.junit.After;
import org.junit.Before;

import com.sandwich.koan.constant.KoanConstants;


public class FileMonitorTest {

	String SAMPLE_DIR = FileUtils.makeAbsoluteRelativeTo(KoanConstants.PROJ_TESTS_FOLDER);
	FileMonitor monitor;
	
	@Before
	public void createInstance() throws Exception{
		monitor = FileMonitorFactory.getInstance(SAMPLE_DIR);
	}
	
	@After
	public void destroyInstance(){
		monitor.close();
	}
	
}
