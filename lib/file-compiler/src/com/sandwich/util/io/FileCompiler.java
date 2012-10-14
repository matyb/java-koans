package com.sandwich.util.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.sandwich.util.io.ui.DefaultErrorPresenter;
import com.sandwich.util.io.ui.ErrorPresenter;

public class FileCompiler {
	
	private static final String DOLLAR_SIGN = "$";
	public static final String JAVA_SUFFIX = ".java";
	public static final String CLASS_SUFFIX = ".class";

	public static void compile(String src, String bin) throws IOException {
		compile(new DefaultErrorPresenter(), new File(src), new File(bin));
	}
	
	public static void compile(ErrorPresenter errorPresenter, String src, String bin) throws IOException {
		compile(errorPresenter, new File(src), new File(bin));
	}

	public static void compile(ErrorPresenter errorPresenter, File src, File bin, 
			final String...classpath) throws IOException {
		compile(src, bin, new CompilationFailureLogger(errorPresenter), classpath);
	}

	public static void compile(File src, final File bin, 
			CompilationListener listener, String[] classpath) throws IOException {
		compile(src, bin, listener, 5000l, classpath);
	}
	
	public static void compile(File src, File bin,
			CompilationListener listener, long timeout, String[] classpath) throws IOException {
		if(!bin.exists()){
			if(!bin.mkdir()){
				System.err.println("Was unable to create: "+bin);
				System.exit(-231);
			}
		}
		FileUtils.forEachFile(src, bin, new FileCompilerAction(bin, listener, timeout, classpath));
	}

	public static String getContentsOfJavaFile(String sourceDir, String className) {
		return FileUtils.readFileAsString(getSourceFileFromClass(sourceDir, className));
	}
	
	public static File getSourceFileFromClass(String sourceDir, String className) {
		File sourceFile = new File(
				  sourceDir + System.getProperty("file.separator") + 
				  classNameToJavaFileName(className));
		if (!sourceFile.exists()) {
			throw new IllegalArgumentException(new FileNotFoundException(
					sourceFile.getAbsolutePath() + " does not exist"));
		}
		return sourceFile;
	}

	public static String classNameToJavaFileName(String className) {
		className = className.replace(".", System.getProperty("file.separator"));
		if(className.contains(DOLLAR_SIGN)){
			className = className.substring(0, className.indexOf(DOLLAR_SIGN));
		}
		return className + JAVA_SUFFIX;
	}

	public static File sourceToClass(String sourceDir, String binDir, File file) {
		return new File(file.getAbsolutePath()
				.replace(sourceDir, binDir).replace(JAVA_SUFFIX, CLASS_SUFFIX));
	}
	
	public static File classToSource(String binDir, String sourceDir, File file) {
		return classToSource(binDir, sourceDir, file.getAbsolutePath());
	}

	public static File classToClassFile(Class<?> clazz) {
		String className = clazz.getName();
		String path = className.replace(".", System.getProperty("file.separator")) + CLASS_SUFFIX;
		return new File(ClassLoader.getSystemResource(path).toString().substring(5));
	}
	
	public static File classToSource(String binDir, String sourceDir, String absolutePath) {
		return new File(absolutePath
				.replace(binDir, sourceDir).replace(CLASS_SUFFIX, JAVA_SUFFIX));
	}
	
}
