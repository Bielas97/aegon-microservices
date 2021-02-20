package com.aegon.application;

import java.util.Arrays;

public enum SupportedFileExtension {
	CSV,
	XLSX;

	public static SupportedFileExtension from(String extension) {
		final String extensionUpperCase = extension.toUpperCase();
		return Arrays.stream(SupportedFileExtension.values())
				.map(Enum::toString)
				.filter(supportedExtension -> supportedExtension.equals(extensionUpperCase))
				.findFirst()
				.map(SupportedFileExtension::valueOf)
				.orElseThrow(NotSupportedFileExtensionException::getInstance);
	}

}
