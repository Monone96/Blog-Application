package it.course.myblogc3.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="ADVISORY_REASON")
@Data @AllArgsConstructor @NoArgsConstructor
public class AdvisoryReason {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
		
	@Column(name="ADVISORY_REASON_NAME", length=15, unique=true, nullable=false)
	private String advisoryReasonName ;

	public AdvisoryReason(String advisoryReasonName) {
		super();
		this.advisoryReasonName = advisoryReasonName;
	}
	
	
	
}
