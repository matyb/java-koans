package com.sandwich.util.io;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class FileMonitorTest {

	String SAMPLE_DIR = FileUtils.makeAbsoluteRelativeTo("incremental-builder");
	FileMonitor monitor;
	
	@Before
	public void createInstance() throws Exception{
		monitor = FileMonitor.getInstance(SAMPLE_DIR, 1);
	}
	
	@After
	public void destroyInstance(){
		monitor.close();
	}
	
	@Test
	public void testHasChanged_noChange() throws Exception {
		assertFalse(monitor.hasChanged());
	}
	
	@Test
	public void testHasChanged_fileModified() throws Exception {
		monitor.hash = 0l;
		assertTrue(monitor.hasChanged());
	}
	
	@Test
	public void testReset() throws Exception {
		monitor.hash = 0l;
		monitor.reset();
		assertFalse(monitor.hasChanged());
	}
}
