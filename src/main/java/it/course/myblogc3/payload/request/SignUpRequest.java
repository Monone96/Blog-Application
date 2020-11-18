package it.course.myblogc3.payload.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class SignUpRequest {
	
	@Email
	@Size(max=120, min=6)
	@NotBlank
	private String email;
	
	@Size(max=12, min=3)
	@NotBlank
	private String username;
	
	@Size(min=5, max=15)
	@NotBlank
	private String password;

}
