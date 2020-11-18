package it.course.myblogc3.entity;

import javax.persistence.Column;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;


import it.course.myblogc3.entity.audit.DateAudit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name="ADVISORY")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Advisory extends DateAudit {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	@Column(name="ADVISORY_ID")
	private AdvisoryId advisoryId;
	
	@Column(name="STATUS", columnDefinition="TINYINT(1)", nullable=false)
	@Enumerated(EnumType.ORDINAL)
	private AdvisoryStatus status;
	
	@Column(length = 100)
	private String description;
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((advisoryId == null) ? 0 : advisoryId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof Advisory))
			return false;
		Advisory other = (Advisory) obj;
		if (advisoryId == null) {
			if (other.advisoryId != null)
				return false;
		} else if (!advisoryId.equals(other.advisoryId))
			return false;
		return true;
	}	
	
}
