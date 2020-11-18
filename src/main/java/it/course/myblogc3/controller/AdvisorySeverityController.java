package it.course.myblogc3.controller;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import it.course.myblogc3.entity.AdvisorySeverity;
import it.course.myblogc3.payload.request.AdvisorySeverityRequest;
import it.course.myblogc3.payload.response.ApiResponseCustom;
import it.course.myblogc3.repository.AdvisorySeverityRepository;


@RestController
public class AdvisorySeverityController {
	
	@Autowired AdvisorySeverityRepository advisorySeverityRepository;

	@PostMapping("private/create-advisory-severity")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponseCustom> createAdvisorySeverity(@Valid @RequestBody AdvisorySeverityRequest advisorySeverityRequest, HttpServletRequest request){
		
		Optional<AdvisorySeverity> a = advisorySeverityRepository.findById(advisorySeverityRequest.getSeverityDescription());
		if(a.isPresent()) {
			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(Instant.now(),200,"OK", "Severity already present",request.getRequestURI()
					), HttpStatus.OK);
		}
		
		AdvisorySeverity as = new AdvisorySeverity(
				advisorySeverityRequest.getSeverityDescription().toUpperCase(),
				advisorySeverityRequest.getSeverityValue()
				);
		
		advisorySeverityRepository.save(as);
		
		return new ResponseEntity<ApiResponseCustom>(
				new ApiResponseCustom(Instant.now(),200,"OK", "New severity added",request.getRequestURI()
				), HttpStatus.OK);
		
	}
	
	@GetMapping("private/get-advisory-severity")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponseCustom>getAdvisorySeverity(HttpServletRequest request){
		
		List<AdvisorySeverity> asl = advisorySeverityRepository.findAllByOrderBySeverityValueDesc();
		
		if(asl.isEmpty()) {
			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(Instant.now(),200, "No values present", asl,request.getRequestURI()
					), HttpStatus.OK);
		}
		
		return new ResponseEntity<ApiResponseCustom>(
				new ApiResponseCustom(Instant.now(),200,"OK", asl,request.getRequestURI()
				), HttpStatus.OK);
		
	}
	

}
