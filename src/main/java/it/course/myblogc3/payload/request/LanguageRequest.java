package it.course.myblogc3.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class LanguageRequest {
	
	@NotBlank
	@Size(min=2, max=2)
	private String langCode;
	
	@NotBlank
	@Size(max=15)
	private String langDesc;

}
