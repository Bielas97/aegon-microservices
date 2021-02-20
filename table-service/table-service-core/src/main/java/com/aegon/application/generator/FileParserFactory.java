package com.aegon.application.generator;

import java.io.File;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
class FileParserFactory {

	@Value("classpath:tables/DefaultTables.csv")
	private Resource resourceFile;

	public FileParser create(File file) {
		// TODO to implement
		return new CSVFileParser(file);
	}

	public FileParser defaultParser() {
		try {
			return create(resourceFile.getFile());
		} catch (IOException e) {
			throw TableGeneratorException.of(e.getMessage());
		}
	}

}
