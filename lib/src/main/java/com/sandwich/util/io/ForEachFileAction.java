package com.sandwich.util.io;

import java.io.File;
import java.io.IOException;

public abstract class ForEachFileAction extends ExistingFileAction {

	public ForEachFileAction(String... strings) {
		super(strings);
	}

	public void onDirectory(File dir) throws IOException {
		for (String fileName : dir.list()) {
			operate(new File(dir, fileName));
		}
	}

}
