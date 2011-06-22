package com.sandwich.util.io;

import static com.sandwich.util.io.IOConstants.FILESYSTEM_SEPARATOR;
import static com.sandwich.util.io.IOConstants.PERIOD;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import com.sandwich.koan.constant.KoanConstants;

public class FileUtils {
	
	public static String BASE_DIR = new File(ClassLoader.getSystemResource(".").getFile()).getParentFile().getParent();
	private static final Map<Class<?>,String> FILE_CONTENTS_BY_CLASS_CACHE = new HashMap<Class<?>,String>();
	
	private static String PROJECT_DIRECTORY = KoanConstants.PROJ_MAIN_FOLDER; 
	
	public static void setToTest(){
		PROJECT_DIRECTORY = KoanConstants.PROJ_TESTS_FOLDER;
	}
	
	public static void setToProd(){
		PROJECT_DIRECTORY = KoanConstants.PROJ_MAIN_FOLDER;
	}
	
	public static String getProjectDirectory(){
		return PROJECT_DIRECTORY;
	}
	
	public static String makeAbsoluteRelativeToProject(){
		return makeAbsoluteRelativeTo(getProjectDirectory());
	}
	
	public static String makeAbsoluteRelativeTo(String fileName){
		StringBuilder builder = new StringBuilder(BASE_DIR);
		return builder.append(FILESYSTEM_SEPARATOR).append(fileName).toString();
	}

	public static void copyRelative(String fileName0, String fileName1) throws IOException{
		copyAbsolute(makeAbsoluteRelativeTo(fileName0), makeAbsoluteRelativeTo(fileName1));
	}

	public static void copyAbsolute(String fileName0, String fileName1) throws IOException {
		copy(new File(fileName0), new File(fileName1));
	}
	
	public static void copy(final File file0, final File file1) throws IOException{
		forEachFile(file0, file1, new FileAction(){
			public void sourceToDestination(File src, File dest) throws IOException {
				InputStream in = new FileInputStream(src);
				OutputStream out = new FileOutputStream(dest);
				
				// Transfer bytes from in to out
				byte[] buf = new byte[1024];
				int len;

				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}

				in.close();
				out.close();
			}
			public File makeDestination(File dest, String fileInDirectory) {
				if(!dest.exists()){
					dest.mkdirs();
				}
				return new File(dest, fileInDirectory);
			}
		});
	}

	public static void forEachFile(File src, File dest, FileAction fileAction) throws IOException {
		if((src == null || !src.exists()) && dest == null){
			throw new IllegalArgumentException("Both path's must actually exist");
		}
		if (src.isDirectory()) {
			if (!dest.exists()) {
				dest.mkdir();
			}
			String files[] = src.list();
			for (int i = 0; i < files.length; i++) {
				forEachFile(new File(src, files[i]), fileAction.makeDestination(dest, files[i]), fileAction);
			}
		} else {
			if (!src.exists()) {
				throw new IOException("File or directory does not exist: "+src);
			} else {
				fileAction.sourceToDestination(src, dest);
			}
		}
	}

	public static String readFileAsString(File file) {
	    byte[] buffer = new byte[(int) file.length()];
	    BufferedInputStream f = null;
	    try {
	        f = new BufferedInputStream(new FileInputStream(file));
	        f.read(buffer);
	    } catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
	        if (f != null) try { f.close(); } catch (IOException ignored) { }
	    }
	    return new String(buffer);
	}
	
	public static String getContentsOfOriginalJavaFile(String projectMainFolder, String sourceFolder, Class<?> declaringClass) {
		String contents = FILE_CONTENTS_BY_CLASS_CACHE.get(declaringClass);
		if(contents == null){
			File sourceFile = new File(makeAbsolute(projectMainFolder, sourceFolder, declaringClass));
			if(!sourceFile.exists()){
				throw new RuntimeException(new FileNotFoundException(sourceFile.getAbsolutePath()+" does not exist"));
			}
			contents = readFileAsString(sourceFile);
			FILE_CONTENTS_BY_CLASS_CACHE.put(declaringClass, contents);
		}
		return contents;
	}

	public static String getJavaFileNameFromClass(Class<?> declaringClass, String inferredPackageNamePlusPeriod) {
		StringBuffer className = new StringBuffer(declaringClass.getName());
		if(!declaringClass.isAnonymousClass()){
			return className.substring(inferredPackageNamePlusPeriod.length(), className.length());
		}
		int dollarIndex = className.indexOf("$");
		className.replace(dollarIndex, className.length(), "");
		className.replace(0, declaringClass.getPackage().getName().length() + 1, "");
		return className.toString();
	}
	
	private static String makeAbsolute(String projectMainFolder, String sourceFolder, Class<?> declaringClass) {
		String className = declaringClass.getName();
		String removedText = declaringClass.isAnonymousClass() ? 
				className.substring(className.lastIndexOf(KoanConstants.PERIOD) + 1) : declaringClass.getSimpleName();
		String inferredPackageNamePlusPeriod = className.replace(removedText, "");
		return makeAbsoluteRelativeTo(new StringBuilder(projectMainFolder).append(FILESYSTEM_SEPARATOR)
				.append(sourceFolder).append(FILESYSTEM_SEPARATOR)
				.append(inferredPackageNamePlusPeriod.replace(PERIOD, FILESYSTEM_SEPARATOR)).append(FILESYSTEM_SEPARATOR)
				.append(getJavaFileNameFromClass(declaringClass,inferredPackageNamePlusPeriod)).append(".java").toString());
	}

	public static File sourceToClass(File file) {
		return new File(file.getAbsolutePath()
				.replace(KoanConstants.SOURCE_FOLDER, KoanConstants.BIN_FOLDER)
				.replace(FileCompiler.JAVA_SUFFIX, FileCompiler.CLASS_SUFFIX));
	}
	
	public static File classToSource(File file) {
		return new File(file.getAbsolutePath()
				.replace(KoanConstants.BIN_FOLDER, KoanConstants.SOURCE_FOLDER)
				.replace(FileCompiler.CLASS_SUFFIX, FileCompiler.JAVA_SUFFIX));
	}
	
}
