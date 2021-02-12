package com.aegon.domain;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "kv_tables")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MongoKvTableDocument {

	@Id
	private String id;

	@Indexed(unique = true)
	private String name;

	private Integer maxPlaces;

	private Set<String> customerIds;

}
