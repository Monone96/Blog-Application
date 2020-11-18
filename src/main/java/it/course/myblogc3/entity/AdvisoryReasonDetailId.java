package it.course.myblogc3.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data @AllArgsConstructor @NoArgsConstructor
public class AdvisoryReasonDetailId implements Serializable{

	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ADVISORY_REASON", nullable=false)
	private AdvisoryReason advisoryReason;

	@Column(name="START_DATE", nullable=false, columnDefinition="DATE")
	private Date startDate;
	
	
	
	
}
