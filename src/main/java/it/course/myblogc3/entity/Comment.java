package it.course.myblogc3.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="COMMENT")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Comment {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
		
	@Column(name="CREATED_AT", 
			updatable=false, insertable=false, 
			columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Date createdAt;
	
	@Column(name="COMMENT", nullable=false, length=200)
	private String comment;
	
	@Column(name="IS_VISIBLE", nullable=false)
	private Boolean visible = true;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="COMMENT_AUTHOR", nullable=false)
	private User commentAuthor;

	@ManyToOne(fetch=FetchType.LAZY)
	private Post post;
	
	public Comment(String comment, Post post, User commentAuthor) {
		super();
		this.comment = comment;
		this.post = post;
		this.commentAuthor = commentAuthor;
	}
	
	public Comment(String comment, Post post, User commentAuthor, boolean visible) {
		super();
		this.comment = comment;
		this.post = post;
		this.commentAuthor = commentAuthor;
		this.visible = visible;
	}
	
	public Comment(Long id, Date createdAt, String comment, Boolean visible) {
		super();
		this.id = id;
		this.createdAt = createdAt;
		this.comment = comment;
		this.visible = visible;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((comment == null) ? 0 : comment.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Comment other = (Comment) obj;
		if (comment == null) {
			if (other.comment != null)
				return false;
		} else if (!comment.equals(other.comment))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}


}
