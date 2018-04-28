package com.sandwich.util.io;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.sandwich.util.io.directories.DirectoryManager;

public class FileOperationTest {

	@Test
	public void givenAnExistingFileNoFilterIsNotIgnored() throws Exception {
		assertFalse(new FileOperation() {
			public void onDirectory(File dir) throws IOException {}
			public void onFile(File file) throws IOException {}
			public void onNull(File nullFile) throws IOException {}
			public void onNew(File newFile) throws IOException {}
		}.isIgnored(new File(DirectoryManager.getBinDir())));
	}
	
	@Test
	public void givenAnExistingFileWithNonMatchingFilterIsNotIgnored() throws Exception {
		assertFalse(new FileOperation("nowayimpartofafoldername") {
			public void onDirectory(File dir) throws IOException {}
			public void onFile(File file) throws IOException {}
			public void onNull(File nullFile) throws IOException {}
			public void onNew(File newFile) throws IOException {}
		}.isIgnored(new File(DirectoryManager.getBinDir())));
	}
	
	@Test
	public void givenAnExistingFileWithMatchingFilterIsIgnored() throws Exception {
		assertTrue(new FileOperation("bin") {
			public void onDirectory(File dir) throws IOException {}
			public void onFile(File file) throws IOException {}
			public void onNull(File nullFile) throws IOException {}
			public void onNew(File newFile) throws IOException {}
		}.isIgnored(new File(DirectoryManager.getBinDir())));
	}
	
	@Test
	public void givenAnExistingFileWithFilterMatchingOnParentFolderIsIgnored() throws Exception {
		assertTrue(new FileOperation("app") {
			public void onDirectory(File dir) throws IOException {}
			public void onFile(File file) throws IOException {}
			public void onNull(File nullFile) throws IOException {}
			public void onNew(File newFile) throws IOException {}
		}.isIgnored(new File(DirectoryManager.getBinDir())));
	}
	
	@Test
	public void givenAnExistingFileWithPartiallyMatchingFilterIsNotIgnored() throws Exception {
		assertFalse(new FileOperation("bi") {
			public void onDirectory(File dir) throws IOException {}
			public void onFile(File file) throws IOException {}
			public void onNull(File nullFile) throws IOException {}
			public void onNew(File newFile) throws IOException {}
		}.isIgnored(new File(DirectoryManager.getBinDir())));
	}

	@Test
	public void givenAnExistingFileWithRegexMatchingFilterIsIgnored() throws Exception {
		assertTrue(new FileOperation("b.n") {
			public void onDirectory(File dir) throws IOException {}
			public void onFile(File file) throws IOException {}
			public void onNull(File nullFile) throws IOException {}
			public void onNew(File newFile) throws IOException {}
		}.isIgnored(new File(DirectoryManager.getBinDir())));
	}
	
	@Test
	public void givenAnExistingFileWithRegexMatchingFilterIsIgnored_compliment() throws Exception {
		assertFalse(new FileOperation("nowayimpart..afoldername") {
			public void onDirectory(File dir) throws IOException {}
			public void onFile(File file) throws IOException {}
			public void onNull(File nullFile) throws IOException {}
			public void onNew(File newFile) throws IOException {}
		}.isIgnored(new File(DirectoryManager.getBinDir())));
	}
	
	@Test
	public void givenIntellijFoldersSpecificallyFileInIdeaFolderAreIgnored() throws Exception {
		// not sure why idea ide's would be a problem... TODO: testing this specifically smells like a bigger problem...
		assertTrue(new FileOperation(".*\\.idea") {
			public void onDirectory(File dir) throws IOException {}
			public void onFile(File file) throws IOException {}
			public void onNull(File nullFile) throws IOException {}
			public void onNew(File newFile) throws IOException {}
		}.isIgnored(new File("/java-dojo/labs/01-koans/java-koans\\koans.idea/workspace.xml___jb_old___")));
	}
	
}
