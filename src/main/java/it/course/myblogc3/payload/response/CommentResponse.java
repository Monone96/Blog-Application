package it.course.myblogc3.payload.response;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class CommentResponse {

	private long id;
	private Date createdAt;
	private String comment;
	private boolean visible;
	private String authorUsername;
	private String postTitle;
	
}
