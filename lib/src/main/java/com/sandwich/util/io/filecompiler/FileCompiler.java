package com.sandwich.util.io.filecompiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.sandwich.util.io.FileUtils;
import com.sandwich.util.io.ui.DefaultErrorPresenter;
import com.sandwich.util.io.ui.ErrorPresenter;

public class FileCompiler {
	
	private static final Map<String, String> sourceFileToClassFile = new HashMap<String, String>();
	private static final Map<String, String> classFileToSourceFile = new HashMap<String, String>();
	
	private static final String DOLLAR_SIGN = "$";
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
	
	public static void compile(File src, final File bin, final CompilationListener listener, 
			final long timeout, final String[] classpath) throws IOException {
		if(!bin.exists()){
			if(!bin.mkdir()){
				System.err.println("Was unable to create: "+bin);
				System.exit(-231);
			}
		}
		new FileCompilerAction(bin, listener, timeout, classpath).operate(src);;
		String srcPath = src.getAbsolutePath();
		String classPath = srcPath;
		for(String suffix : CompilerConfig.getSupportedFileSuffixes()){
			if(classPath.endsWith(suffix)){
				classPath = classPath.replace(suffix, CLASS_SUFFIX);
			}
		}
		sourceFileToClassFile.put(srcPath, classPath);
		classFileToSourceFile.put(classPath, srcPath);
	}

	public static String getContentsOfJavaFile(String sourceDir, String className) {
		return FileUtils.readFileAsString(getSourceFileFromClass(sourceDir, className));
	}
	
	public static File getSourceFileFromClass(String sourceDir, String className) {
		if(className.contains(DOLLAR_SIGN)){
			className = className.substring(0, className.indexOf(DOLLAR_SIGN));
		}

		File possibleSourceFile = new File(sourceDir);
		File sourceFile = null;
		for(String folder : className.split("\\.")){
			possibleSourceFile = new File(possibleSourceFile, folder);
		}

		for(String suffix : CompilerConfig.getSupportedFileSuffixes()){
			File file = new File(possibleSourceFile.getAbsolutePath() + suffix);
			if(file.exists()){
				sourceFile = file;
				break;
			}
		}
		if (sourceFile == null || !sourceFile.exists()) {
			throw new IllegalArgumentException(new FileNotFoundException(
					sourceFile == null ? null : sourceFile.getAbsolutePath() + " does not exist"));
		}
		return sourceFile;
	}

	public static File sourceToClass(String sourceDir, String binDir, File file) {
		//C:\Users\sandwich\Development\koans\koans\app\bin\beginner\AboutKoans.class
		String classPath = file.getAbsolutePath().replace(sourceDir, binDir);
		for(String suffix : CompilerConfig.getSupportedFileSuffixes()){
			classPath = classPath.replace(suffix, "");
		}
		return new File(classPath + CLASS_SUFFIX);
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
		String sourcePath = absolutePath.replace(binDir, sourceDir).replace(CLASS_SUFFIX, "");
		for(String suffix : CompilerConfig.getSupportedFileSuffixes()){
			File file = new File(sourcePath + suffix);
			if(file.exists()){
				return file;
			}
		}
		return new File(sourcePath);
	}
	
}
