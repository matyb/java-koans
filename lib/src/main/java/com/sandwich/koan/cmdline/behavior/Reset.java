package com.sandwich.koan.cmdline.behavior;

import com.sandwich.util.io.CopyFileOperation;

import java.io.IOException;

public class Reset extends KoanFileCopying {

    @Override
    protected void copy(String backupSrcDirectory, String appSrcDirectory)
            throws IOException {
        new CopyFileOperation(backupSrcDirectory, appSrcDirectory).operate();
    }

}
