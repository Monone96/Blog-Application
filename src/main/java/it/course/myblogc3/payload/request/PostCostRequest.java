package it.course.myblogc3.payload.request;

import java.util.Date;

import javax.validation.constraints.Future;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sun.istack.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class PostCostRequest {

	private long postId;
	
	@NotNull
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date startDate;
	
	@Future
	@NotNull
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date endDate;
	
	@NotNull
	private int shiftCost;
	
}
