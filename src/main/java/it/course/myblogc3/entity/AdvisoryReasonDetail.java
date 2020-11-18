package it.course.myblogc3.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="ADVISORY_REASON_DETAIL")
@Data @AllArgsConstructor @NoArgsConstructor
public class AdvisoryReasonDetail implements Serializable{

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	AdvisoryReasonDetailId advisoryReasonDetailId;
	
	@Column(name="END_DATE", nullable=false, columnDefinition="DATE")
	private Date endDate;

	@ManyToOne
	@JoinColumn(name="ADVISORY_SEVERITY", nullable=false)
	private AdvisorySeverity advisorySeverity;
	
}
