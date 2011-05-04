package com.sandwich.util.io;

import static com.sandwich.util.io.IOConstants.FILESYSTEM_SEPARATOR;

import java.io.File;
import java.io.IOException;
import java.net.URI;

public class FileCompiler {
	
	public static final String JAVA_SUFFIX = ".java";
	public static final String CLASS_SUFFIX = 	".class";

	public static void compileRelative(String parentFolder, String src, String bin) throws IOException{
		compileAbsolute(
			FileUtils.makeAbsoluteRelativeTo(new StringBuilder(parentFolder).append(FILESYSTEM_SEPARATOR).append(src).toString()),
			FileUtils.makeAbsoluteRelativeTo(new StringBuilder(parentFolder).append(FILESYSTEM_SEPARATOR).append(bin).toString()));
	}

	public static void compileAbsolute(String src, String bin) throws IOException {
		compile(new File(src), new File(bin));
	}

	public static void compile(File src, File bin, final String...classpath) throws IOException {
		final File destinationDirectory = bin;
		FileUtils.forEachFile(src, bin, new FileAction(){
			public void sourceToDestination(File src, File bin) throws IOException {
				//javac -d ..\bin -classpath ..\lib\koans.jar beginner\*.java
				String fileName = src.getName();
				if(fileName.length() > 4 && fileName.toLowerCase().endsWith(JAVA_SUFFIX)){
					String command = "javac -d "+destinationDirectory.getAbsolutePath()+" "+
						getClasspath(classpath)+src.getAbsolutePath();
					Process p = Runtime.getRuntime().exec(command);
					try{
						if(p.waitFor() != 0){
							System.out.println(command);
							System.out.println(src.getAbsolutePath()+" does not compile. exit status was: "+p.exitValue());
							Thread.sleep(4000);
						}
					} catch (Exception x) {
						x.printStackTrace();
					}
				}
			}
			private String getClasspath(String[] classpaths) {
				StringBuilder builder = new StringBuilder();
				if(classpaths.length > 0){
					builder.append(" -classpath ");
				}
				for(String classpath : classpaths){
					builder.append(classpath).append(" ");
				}
				return builder.toString();
			}
			public File makeDestination(File dest, String fileInDirectory) {
				String fileName = dest.getName();
				fileName = fileName.length() > 4 && fileName.toLowerCase().contains(JAVA_SUFFIX) ? 
						fileName.replace(JAVA_SUFFIX, CLASS_SUFFIX) : fileName;
				dest = new File(dest, fileInDirectory);
				System.out.println("file: "+dest.getAbsolutePath());
				return dest;
			}
		});
	}

	public static Class<?> loadClass(File javaFile, String sourceFolder, String binFolder) {
		String absolutePath = javaFile.getAbsolutePath();
		URI url = new File(absolutePath.replace(
				sourceFolder, binFolder).replace(
						FileCompiler.JAVA_SUFFIX, FileCompiler.CLASS_SUFFIX)).toURI();
		DynamicClassLoader cl = new DynamicClassLoader(FileCompiler.class.getClassLoader());
		sourceFolder = sourceFolder.endsWith(IOConstants.FILESYSTEM_SEPARATOR) ? sourceFolder : 
			(sourceFolder+IOConstants.FILESYSTEM_SEPARATOR);
		String javaClassName = absolutePath.substring(absolutePath.indexOf(
				sourceFolder) + sourceFolder.length(), absolutePath.length() - FileCompiler.JAVA_SUFFIX.length())
				.replace(IOConstants.FILESYSTEM_SEPARATOR, IOConstants.PERIOD);
		try {
			return cl.loadClass(url.toURL(), javaClassName);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}
