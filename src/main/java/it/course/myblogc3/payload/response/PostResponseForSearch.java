package it.course.myblogc3.payload.response;

import java.util.Date;

import it.course.myblogc3.payload.response.PostResponseForSearch;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class PostResponseForSearch {
	
	private Long id;
	private String title;
	private String content;
	private String authorName;
	private Date updatedAt;

	
}
