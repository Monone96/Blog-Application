package it.course.myblogc3.payload.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class UpdateMeRequest {
	
	@Size(max=120, min=3, message="username length must be between 3 and 12 chars")
	@NotBlank(message="User must not be blank")
	private String username;
	
	@Email
	@Size(max=120, min=6)
	@NotBlank
	private String email;

}
