package it.course.myblogc3.payload.response;

import java.util.Date;

import it.course.myblogc3.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class PostResponseWithPreferredTags {

	private Long id;
	private String title;
	private Long authorId;
	private String authorName;
	private Date updatedAt;
	private String[] tagNames;
	
	public static PostResponseWithPreferredTags createFromEntity(Post p) {
		return new PostResponseWithPreferredTags(
				p.getId(),
				p.getTitle(),
				p.getAuthor().getId(),
				p.getAuthor().getUsername(),
				p.getUpdatedAt(),
				p.getTags().stream().map(t -> t.getTagName()).toArray(String[]::new)
				);
	}
	
	
	
}
