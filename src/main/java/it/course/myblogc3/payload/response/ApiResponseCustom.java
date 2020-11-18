package it.course.myblogc3.payload.response;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class ApiResponseCustom {

	private Instant timestamp;
	
	private int status;
	
	private String error;
	
	private Object message;
	
	private String path;
	
}
