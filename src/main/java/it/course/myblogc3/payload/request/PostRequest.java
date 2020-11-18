package it.course.myblogc3.payload.request;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class PostRequest {
	
	@NotBlank @NotEmpty
	@Size(max=100)
	private String title;
	
	@NotBlank @NotEmpty
	private String content;
		
	@NotBlank @NotEmpty
	@Size(min=2, max=2)
	private String language;
	
	Set<String> tagNames = new HashSet<String>();

	public PostRequest(@NotBlank @Size(max = 100) String title, @NotBlank String content,
			 @NotBlank @Size(min = 2, max = 2) String language) {
		super();
		this.title = title;
		this.content = content;
		this.language = language;
	}
	
	

}
