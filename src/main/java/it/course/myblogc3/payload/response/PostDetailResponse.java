package it.course.myblogc3.payload.response;

import java.util.Date;
import java.util.List;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class PostDetailResponse {
	
	private Long id;
	private String title;
	private String content;
	private Long authorId;
	private String authorName;
	private Date updatedAt;
	private double average;
	private List<CommentResponseForPostDetail> comments;
	private Set<String> tags;
	
	public PostDetailResponse(Long id, String title, String content, Long authorId, String authorName, Date updatedAt, double average) {
		super();
		this.id = id;
		this.title = title;
		this.content = content;
		this.authorId = authorId;
		this.authorName = authorName;
		this.updatedAt = updatedAt;
		this.average = average;
	}

}