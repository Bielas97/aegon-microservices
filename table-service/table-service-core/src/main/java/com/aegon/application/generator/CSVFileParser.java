package com.aegon.application.generator;

import com.aegon.domain.TableName;
import com.aegon.requests.AddNewTableRequest;
import com.opencsv.CSVReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;
import reactor.core.publisher.Flux;

class CSVFileParser implements FileParser {

	private final File csv;

	public CSVFileParser(File file) {
		//		validateFileIsCsv()
		this.csv = file;
	}

	@Override
	public Flux<AddNewTableRequest> parse() {
		return Flux.using(
				() -> parseFile(csv),
				Flux::fromStream,
				Stream::close
		);
	}

	private Stream<AddNewTableRequest> parseFile(File file) throws IOException {
		final Set<AddNewTableRequest> requests = new HashSet<>();
		final CSVReader csvReader = new CSVReader(new FileReader(file));
		String[] values;
		while ((values = csvReader.readNext()) != null) {
			final Integer maxPlaces = Integer.valueOf(values[1]);
			requests.add(AddNewTableRequest.builder()
					.name(TableName.from(values[0]))
					.maxPlaces(maxPlaces)
					.build());
		}
		return requests.stream();
	}
}
