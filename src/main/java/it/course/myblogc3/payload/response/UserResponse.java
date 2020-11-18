package it.course.myblogc3.payload.response;

import java.util.Set;

import it.course.myblogc3.entity.AuthorityName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class UserResponse {
	
	private Long id;
	private String username;
	private String email;
	private boolean enabled;
	private Set<AuthorityName> authorityNames;

}
