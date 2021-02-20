package com.aegon.application.generator;

import com.aegon.domain.TableName;
import com.aegon.requests.AddNewTableRequest;
import com.aegon.util.lang.Preconditions;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;

class TextFileDataBufferParser implements DataBufferParser {

	private final DataBuffer dataBuffer;

	public TextFileDataBufferParser(DataBuffer dataBuffer) {
		this.dataBuffer = Preconditions.requireNonNull(dataBuffer);
	}

	@Override
	public List<AddNewTableRequest> parse() {
		final byte[] bytes = new byte[dataBuffer.readableByteCount()];
		dataBuffer.read(bytes);
		DataBufferUtils.release(dataBuffer);
		final String fileAsString = new String(bytes, StandardCharsets.UTF_8);
		validateLinesFormat(fileAsString);
		return fileAsString.lines().filter(line -> !StringUtils.isBlank(line))
				.map(this::parseLine)
				.collect(Collectors.toList());
	}

	private AddNewTableRequest parseLine(String line) {
		final String[] split = line.split(",");
		return AddNewTableRequest.builder()
				.name(TableName.from(split[0]))
				.maxPlaces(Integer.valueOf(split[1]))
				.build();
	}

	private void validateLinesFormat(String file) {
		final boolean isInvalid = file.lines().anyMatch(line -> !TextFileRules.CONSISTS_OF_AT_AND_COMMA_REGEX.matcher(line).matches());
		if (isInvalid) {
			throw FileParserException.of("Wrong format of text file");
		}
	}

}
