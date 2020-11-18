package it.course.myblogc3.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data @AllArgsConstructor @NoArgsConstructor
public class AdvisoryId implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="COMMENT_ID", nullable=false)
	private Comment comment;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="REPORTER", nullable=false)
	private User reporter;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ADVISORY_REASON", nullable=false)
	private AdvisoryReason advisoryReason;

}
