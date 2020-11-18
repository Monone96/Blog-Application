package it.course.myblogc3.payload.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;


@Getter
public class AdvisorySeverityRequest {
	
	@NotNull @NotEmpty
	@Size(max=15)
	private String severityDescription;
	
	@NotNull @Min(1) @Max(100)
	private int severityValue;

}
