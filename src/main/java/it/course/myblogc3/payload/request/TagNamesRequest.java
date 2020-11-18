package it.course.myblogc3.payload.request;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class TagNamesRequest {

	Set<String> tagNames;
	
}
