package it.course.myblogc3.payload.response;

import java.util.Date;

import it.course.myblogc3.entity.AdvisoryStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class AdvisoryResponse {

	private Date createdAt;
	private Date updatedAt;
	private String severityDescription;
	private int severityValue;
	private long advisoryReasonId;
	private String advisoryReasonName;
	private AdvisoryStatus status;
	private String reporter; //reporter (username)
	private String reported; //comment author(username)
	private long commentId;
	
}
