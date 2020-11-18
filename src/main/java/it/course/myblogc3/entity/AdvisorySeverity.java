package it.course.myblogc3.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="ADVISORY_SEVERITY")
@Data @AllArgsConstructor @NoArgsConstructor
public class AdvisorySeverity {
	
	@Id
	@Column(nullable=false, length=15)
	private String severityDescription; // LOW, MEDIUM, HIGH, HIGHEST
	
	@Column(nullable=false, columnDefinition="TINYINT(3)")
	private int severityValue; // 25, 50, 75, 100

}
