package com.aegon.domain;

import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

;

@Document(collection = "application_users")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MongoUserDocument {

	@Id
	private String id;

	@NotBlank
	@Size(max = 20)
	@Indexed(unique = true)
	private String username;

	@NotBlank
	@Email
	@Size(max = 100)
	private String email;

	@NotBlank
	@Size(max = 120)
	private String password;

	private Set<String> roleIds = new HashSet<>();

}
