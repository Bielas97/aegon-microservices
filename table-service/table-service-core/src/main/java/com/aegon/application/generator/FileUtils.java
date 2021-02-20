package com.aegon.application.generator;

import com.aegon.util.lang.Preconditions;
import java.io.File;

public class FileUtils {

	public static String getExtension(File file) {
		Preconditions.requireNonNull(file);
		final String name = file.getName();
		return name.substring(name.lastIndexOf(".") + 1);
	}

}
