package it.course.myblogc3.payload.response;

import java.util.Date;

import it.course.myblogc3.entity.Post;
import it.course.myblogc3.payload.response.PostResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class PostResponse {
	
	private Long id;
	private String title;
	private Long authorId;
	private String authorName;
	private Date updatedAt;
	private int commentsNr;

	public static PostResponse createFromEntity(Post p) {
		return new PostResponse(
			p.getId(),
			p.getTitle(),
			p.getAuthor().getId(),
			p.getAuthor().getUsername(),
			p.getUpdatedAt(),
			p.getComments().size()
			);
		
	}
	
}
