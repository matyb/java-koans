package com.sandwich.koan.runner;

import java.io.File;
import java.net.MalformedURLException;

import com.sandwich.koan.cmdline.CommandLineArgumentBuilder;
import com.sandwich.koan.constant.KoanConstants;
import com.sandwich.koan.path.PathToEnlightenment;
import com.sandwich.util.io.FileCompiler;
import com.sandwich.util.io.FileMonitor;
import com.sandwich.util.io.FileSavedListener;
import com.sandwich.util.io.FileUtils;

public class AppLauncher implements Runnable {

	private final String[] args;
	
	public AppLauncher(String...args){
		this.args = args;
	}
	
	public static void main(final String... args) throws Throwable {
		final AppLauncher app = new AppLauncher(args);
		app.run();
		if(KoanConstants.DEBUG){
			StringBuilder argsBuilder = new StringBuilder();
			int argNumber = 0;
			for(String arg : args){
				argsBuilder.append("Argument number "+String.valueOf(++argNumber)+": '"+arg+"'");
			}
			System.out.println(argsBuilder.toString());
		}
		FileMonitor.getInstance(FileUtils.makeAbsoluteRelativeTo("koans"), 500).addFileSavedListener(
			getFileSavedListener(args));
	}

	private static FileSavedListener getFileSavedListener(final String[] args) {
		return new FileSavedListener(){
			public void fileSaved(File file) {
				String absolutePath = file.getAbsolutePath();
				if(absolutePath.toLowerCase().endsWith(FileCompiler.JAVA_SUFFIX)){
					System.out.println(KoanConstants.EOL+"loading: "+absolutePath);
					try {
						FileCompiler.compile(file, new File(replaceSourceWithBin(absolutePath)), 
								FileUtils.makeAbsoluteRelativeTo(KoanConstants.PROJ_MAIN_FOLDER +
										KoanConstants.FILESYSTEM_SEPARATOR + KoanConstants.LIB_FOLDER + 
										KoanConstants.FILESYSTEM_SEPARATOR + "koans.jar"));
						reloadClassDefinition(file, KoanConstants.SOURCE_FOLDER, KoanConstants.BIN_FOLDER);
						for(int i = 0; i < 80; i++){
							System.out.println();
						}
						new AppLauncher(args).run();
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
			}

			private void reloadClassDefinition(File javaFile, String sourceFolder, String binFolder) throws ClassNotFoundException, MalformedURLException {
				Class<?> cls = FileCompiler.loadClass(javaFile, sourceFolder, binFolder);
				PathToEnlightenment.replace(cls);
			}

			private String replaceSourceWithBin(String absolutePath) {
				return new StringBuilder(absolutePath.substring(0, 
						absolutePath.lastIndexOf(KoanConstants.SOURCE_FOLDER))).append(KoanConstants.FILESYSTEM_SEPARATOR).append(
								KoanConstants.BIN_FOLDER).append(KoanConstants.FILESYSTEM_SEPARATOR).toString();
			}
		};
	}

	
	
	public void run() {
		try{
			new KoanSuiteRunner(
				new CommandLineArgumentBuilder(args).build()	
			).run();
		}catch(Throwable t){
			throw new RuntimeException(t);
		}
	}
	
}
