package com.sandwich.util.io;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.sandwich.koan.path.CommandLineTestCase;
import com.sandwich.util.io.directories.DirectoryManager;


public class DirectoryManagerTest extends CommandLineTestCase {

	@Test
	public void testFileSeparatorInjection_happyPath() throws Exception {
		assertEquals("/home/wilford/liberty", DirectoryManager.injectFileSystemSeparators(
				"home", "wilford", "liberty"));
	}
	
	@Test
	public void testFileSeparatorInjection_separatorsAtBeginning() throws Exception {
		assertEquals("/home/wilford/liberty", DirectoryManager.injectFileSystemSeparators(
				"/home", "/wilford", "/liberty"));
	}
	
	@Test
	public void testFileSeparatorInjection_separatorsAtEnding() throws Exception {
		assertEquals("/home/wilford/liberty", DirectoryManager.injectFileSystemSeparators(
				"home/", "wilford/", "liberty/"));
	}
	
	@Test
	public void testFileSeparatorInjection_separatorsAtBeginningAndEnding() throws Exception {
		assertEquals("/home/wilford/liberty", DirectoryManager.injectFileSystemSeparators(
				"home/", "wilford/", "/liberty/"));
	}
	
	@Test
	public void testFileSeparatorInjection_separatorsInMiddle() throws Exception {
		assertEquals("/home/wilford/liberty", DirectoryManager.injectFileSystemSeparators(
				"home", "wilford/liberty"));
	}
}
