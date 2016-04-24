package com.sandwich.koan.runner;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.sandwich.koan.ApplicationSettings;
import com.sandwich.koan.cmdline.CommandLineArgument;
import com.sandwich.koan.cmdline.CommandLineArgumentBuilder;
import com.sandwich.koan.cmdline.CommandLineArgumentRunner;
import com.sandwich.koan.constant.ArgumentType;
import com.sandwich.koan.util.ApplicationUtils;
import com.sandwich.util.io.FileMonitor;
import com.sandwich.util.io.FileMonitorFactory;
import com.sandwich.util.io.KoanFileCompileAndRunListener;
import com.sandwich.util.io.directories.DirectoryManager;

public class AppLauncher {

    public static void main(final String... args) throws Throwable {
        Map<ArgumentType, CommandLineArgument> argsMap = new CommandLineArgumentBuilder(
                args);
        if (argsMap.containsKey(ArgumentType.RUN_KOANS)) {
            if (ApplicationSettings.isInteractive()) {
                new Thread(new Runnable() {
                    public void run() {
                        do {
                            try {
                                char c = (char) System.in.read();
                                char exitChar = ApplicationSettings.getExitChar();
                                if (Character.toUpperCase(c) == Character.toUpperCase(exitChar)) {
                                    exit(0);
                                }
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        } while (true);
                    }

                }).start();
                FileMonitor monitor = FileMonitorFactory.getInstance(new File(
                        DirectoryManager.getProdMainDir()), new File(
                        DirectoryManager.getDataFile()));
                if (ApplicationUtils.isFirstTimeAppHasBeenRun()) {
                    monitor.writeChanges();
                }
                monitor.addFileSavedListener(new KoanFileCompileAndRunListener(argsMap));
            }
        }
        new CommandLineArgumentRunner(argsMap).run();
        if (ApplicationSettings.isDebug()) {
            StringBuilder argsBuilder = new StringBuilder();
            int argNumber = 0;
            for (String arg : args) {
                argsBuilder.append("Argument number "
                        + String.valueOf(++argNumber) + ": '" + arg + "'");
            }
            ApplicationUtils.getPresenter().displayMessage(
                    argsBuilder.toString());
        }
    }

    static void exit(int status) {
        FileMonitorFactory.closeAll();
        System.exit(status);
    }

}
