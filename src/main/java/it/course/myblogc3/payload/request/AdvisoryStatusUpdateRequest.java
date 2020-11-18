package it.course.myblogc3.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;

@Getter
public class AdvisoryStatusUpdateRequest {
	
	@NotNull
	private long commentId;

	@NotNull
	private long userId;

	@NotNull
	private long reasonId;

	@NotBlank @NotEmpty @Size(max=26)
	private String status;
	
}
