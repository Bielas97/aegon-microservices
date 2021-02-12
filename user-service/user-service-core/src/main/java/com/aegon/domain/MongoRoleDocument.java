package com.aegon.domain;

import com.aegon.util.lang.Preconditions;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "application_roles")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MongoRoleDocument {

	@Id
	private String id;

	@Indexed(unique = true)
	private ApplicationUserRole name;

	private MongoRoleDocument(ApplicationUserRole name) {
		this.name = Preconditions.requireNonNull(name);
	}

	public static MongoRoleDocument from(ApplicationUserRole role) {
		return new MongoRoleDocument(role);
	}

}
