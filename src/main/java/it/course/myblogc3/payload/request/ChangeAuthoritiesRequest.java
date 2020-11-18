package it.course.myblogc3.payload.request;

import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import it.course.myblogc3.entity.AuthorityName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class ChangeAuthoritiesRequest {
	
	@Size(max=12, min=3)
	@NotBlank
	private String username;
	
	private Set<AuthorityName> authorityNames;

}
