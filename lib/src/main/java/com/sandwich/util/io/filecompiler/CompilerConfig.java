package com.sandwich.util.io.filecompiler;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.ResourceBundle;

public class CompilerConfig {

    private static final ResourceBundle commandBySuffixRB = ResourceBundle.getBundle("compilationcommands");
    
    public static boolean isSourceFile(String fileName) {
        // TODO when supporting java > 5, change to return resourcebundle.containsKey(...
        Enumeration<String> keys = commandBySuffixRB.getKeys();
        String suffix = getSuffix(fileName).toLowerCase();
        while(keys.hasMoreElements()){
            String key = keys.nextElement();
            if(key.equals(suffix)){
                return true;
            }
        }
        return false;
    }
    
    public static String[] getCompilationCommand(File src, String destinationPath, String classPath) {
        String absolutePath = src.getAbsolutePath();
        String command = commandBySuffixRB.getString(getSuffix(absolutePath));
        if(command == null){
            throw new RuntimeException("Do not know how to compile " + absolutePath);
        }
        List<String> splitCommand = Arrays.asList(command.split(" "));
        List<String> commandSegments = new ArrayList<String>();
        for(String segment : splitCommand){
            String lowerCaseSegment = segment.toLowerCase();
            if("${bindir}".equals(lowerCaseSegment)){
                commandSegments.add(destinationPath);
            }else if("${classpath}".equals(lowerCaseSegment)){
                commandSegments.add(classPath);
            }else if("${filename}".equals(lowerCaseSegment)){
                commandSegments.add(src.getAbsolutePath());
            }else{
                commandSegments.add(segment);
            }
        }
        return commandSegments.toArray(new String[commandSegments.size()]);
    }

    public static Collection<String> getSupportedFileSuffixes() {
        Enumeration<String> keysEnumeration = commandBySuffixRB.getKeys();
        Collection<String> keys = new ArrayList<String>();
        while(keysEnumeration.hasMoreElements()){
            keys.add(keysEnumeration.nextElement());
        }
        return keys;
    }
    
    public static String getSuffix(String fileName) {
        if(fileName != null){
            int periodIndex = fileName.lastIndexOf('.');
            if(periodIndex > -1){
                return fileName.substring(periodIndex).toLowerCase();
            }
        }
        return "";
    }

    public static boolean isSuffixSupported(String suffix) {
        return getSupportedFileSuffixes().contains(suffix);
    }
    
}
