package it.course.myblogc3.payload.request;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;

@Getter
public class AdvisoryReasonRequest {

	@NotBlank @NotNull @Size(max=15)
	private String advisoryReasonName;
	
	@NotNull
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
	private Date startDate;
	
	//@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
	//private Date endDate;
	
	@NotBlank @NotNull @Size(max=15)
	private String severityDescription;

	
}
