package it.course.myblogc3.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="LOGIN_TRACE")
@Data @AllArgsConstructor @NoArgsConstructor
public class LoginTrace {

	@EmbeddedId
	private LoginTraceId loginTraceId;
	
}
