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

import com.sandwich.koan.constant.KoanConstants;
import com.sandwich.util.io.directories.DirectoryManager;

public class FileUtils {
	
	private static final String DOLLAR_SIGN = "$";
	public static String BASE_DIR; static{ File dir = new File(ClassLoader.getSystemResource(".").getFile());
		if(dir.exists()){
			dir = dir.getParentFile();
			if(dir.exists()){
				dir = dir.getParentFile(); // go up 2 levels from koans/src or koans-tests/src
			}
		}
		BASE_DIR = dir.getAbsolutePath();
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
	
	public static String getContentsOfOriginalJavaFile(String projectMainFolder, String sourceFolder, String className) {
		File sourceFile = new File(makeAbsolute(projectMainFolder, sourceFolder, className));
		if(!sourceFile.exists()){
			throw new RuntimeException(new FileNotFoundException(sourceFile.getAbsolutePath()+" does not exist"));
		}
		return readFileAsString(sourceFile);
	}
	
	public static String getContentsOfOriginalJavaFile(String projectMainFolder, String sourceFolder, Class<?> declaringClass) {
		File sourceFile = new File(makeAbsolute(projectMainFolder, sourceFolder, declaringClass));
		if(!sourceFile.exists()){
			throw new RuntimeException(new FileNotFoundException(sourceFile.getAbsolutePath()+" does not exist"));
		}
		return readFileAsString(sourceFile);
	}

	public static String getContentsOfOriginalJavaFile(String className) {
		File sourceFile = new File(	DirectoryManager.getSourceDir() 
								  + DirectoryManager.FILESYSTEM_SEPARATOR
								  + classNameToJavaFileName(className));
		if(!sourceFile.exists()){
			throw new RuntimeException(new FileNotFoundException(sourceFile.getAbsolutePath()+" does not exist"));
		}
		return readFileAsString(sourceFile);
	}
	
	private static String classNameToJavaFileName(String className) {
		className = className.replace(KoanConstants.PERIOD, DirectoryManager.FILESYSTEM_SEPARATOR);
		if(className.contains(DOLLAR_SIGN)){
			className = className.substring(0, className.indexOf(DOLLAR_SIGN));
		}
		return className + FileCompiler.JAVA_SUFFIX;
	}

	private static String makeAbsolute(String projectMainFolder, String sourceFolder, Class<?> declaringClass) {
		return makeAbsolute(projectMainFolder, sourceFolder, declaringClass.getName());
	}
	
	private static String makeAbsolute(String projectMainFolder, String sourceFolder, String className){
		String removedText = className.substring(className.lastIndexOf(KoanConstants.PERIOD) + 1);
		String inferredPackageNamePlusPeriod = className.replace(removedText, "");
		return makeAbsoluteRelativeTo(new StringBuilder(projectMainFolder).append(FILESYSTEM_SEPARATOR)
				.append(sourceFolder).append(FILESYSTEM_SEPARATOR)
				.append(inferredPackageNamePlusPeriod.replace(PERIOD, FILESYSTEM_SEPARATOR)).append(FILESYSTEM_SEPARATOR)
				.append(getJavaFileNameFromClass(className,inferredPackageNamePlusPeriod)).append(".java").toString());
	}
	
	private static String getJavaFileNameFromClass(String clazzName, String inferredPackageNamePlusPeriod) {
		StringBuffer className = new StringBuffer(clazzName);
		if(!clazzName.contains("$")){
			return className.substring(inferredPackageNamePlusPeriod.length(), className.length());
		}
		int dollarIndex = className.indexOf("$");
		className.replace(dollarIndex, className.length(), "");
		className.replace(0, clazzName.lastIndexOf('.') + 1, "");
		return className.toString();
	}

	public static File sourceToClass(File file) {
		return new File(file.getAbsolutePath()
				.replace(DirectoryManager.getSourceDir(), DirectoryManager.getBinDir())
				.replace(FileCompiler.JAVA_SUFFIX, FileCompiler.CLASS_SUFFIX));
	}
	
	public static File classToSource(File file) {
		return new File(file.getAbsolutePath()
				.replace(DirectoryManager.getBinDir(), DirectoryManager.getSourceDir())
				.replace(FileCompiler.CLASS_SUFFIX, FileCompiler.JAVA_SUFFIX));
	}

	public static String getContentsOfOriginalJavaFile(Class<?> declaringClass0) {
		return getContentsOfOriginalJavaFile(declaringClass0.getName());
	}
	
}
