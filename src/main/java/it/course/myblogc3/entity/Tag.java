package it.course.myblogc3.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="TAG")
@Data @AllArgsConstructor @NoArgsConstructor
public class Tag {
	
	@Id
	@Column(name="TAG_NAME", length=15)
	private String tagName;

	@Column(name="IS_VISIBLE", nullable=false)
	private Boolean visible = true;

	public Tag(String tagName) {
		super();
		this.tagName = tagName;
	}
	
	
	
}
