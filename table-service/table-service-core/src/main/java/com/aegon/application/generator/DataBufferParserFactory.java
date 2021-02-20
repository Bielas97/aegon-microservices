package com.aegon.application.generator;

import com.aegon.application.SupportedFileExtension;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.core.io.buffer.DataBuffer;

class DataBufferParserFactory {

	public static DataBufferParser create(DataBuffer dataBuffer, SupportedFileExtension extension) {
		switch (extension) {
			case XLSX -> throw new NotImplementedException();
			default -> {
				return new TextFileDataBufferParser(dataBuffer);
			}
		}
	}
}
