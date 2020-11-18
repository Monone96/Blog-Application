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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import it.course.myblogc3.entity.AdvisoryReason;
import it.course.myblogc3.entity.AdvisoryReasonDetail;
import it.course.myblogc3.entity.AdvisoryReasonDetailId;
import it.course.myblogc3.entity.AdvisorySeverity;
import it.course.myblogc3.payload.request.AdvisoryReasonRequest;
import it.course.myblogc3.payload.response.AdvisoryReasonResponse;
import it.course.myblogc3.payload.response.ApiResponseCustom;
import it.course.myblogc3.repository.AdvisoryReasonDetailRepository;
import it.course.myblogc3.repository.AdvisoryReasonRepository;
import it.course.myblogc3.repository.AdvisorySeverityRepository;
import it.course.myblogc3.service.AdvisoryService;


@RestController
public class AdvisoryReasonController {
	
	@Autowired AdvisoryReasonRepository advisoryReasonRepository;
	@Autowired AdvisoryReasonDetailRepository advisoryReasonDetailRepository;
	@Autowired AdvisorySeverityRepository advisorySeverityRepository;
	@Autowired AdvisoryService advisoryService;
	

	@PostMapping("private/create-advisory-reason")
	@PreAuthorize("hasRole('ADMIN')")
	@Transactional
	public ResponseEntity<ApiResponseCustom> createAdvisoryReason(@Valid @RequestBody AdvisoryReasonRequest advisoryReasonRequest, HttpServletRequest request){
		
		Optional<AdvisoryReason> ar = advisoryReasonRepository.findByAdvisoryReasonName(advisoryReasonRequest.getAdvisoryReasonName());
		Optional<AdvisorySeverity> as = advisorySeverityRepository.findById(advisoryReasonRequest.getSeverityDescription());
		if(!as.isPresent()) {
			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(Instant.now(), 404, "NOT FOUND", "Severity not found",request.getRequestURI()
					), HttpStatus.NOT_FOUND);
		}
		AdvisoryReason arNew = new AdvisoryReason();
		
		if(ar.isPresent()) {
			Optional<AdvisoryReasonDetail> ard = advisoryReasonDetailRepository.findByAdvisoryReasonDetailIdAdvisoryReasonAndEndDateEquals(ar.get(), advisoryService.fromCalendarToDate());
			ard.get().setEndDate(advisoryReasonRequest.getStartDate());
			AdvisoryReasonDetail ardNew = new AdvisoryReasonDetail(
					new AdvisoryReasonDetailId (ar.get(), advisoryReasonRequest.getStartDate()), advisoryService.fromCalendarToDate(),
					as.get()
					);
			advisoryReasonDetailRepository.save(ardNew);
		} else {
			arNew.setAdvisoryReasonName(advisoryReasonRequest.getAdvisoryReasonName());
			advisoryReasonRepository.save(arNew);
			AdvisoryReasonDetail ardNew = new AdvisoryReasonDetail(
					new AdvisoryReasonDetailId (arNew, advisoryReasonRequest.getStartDate()), advisoryService.fromCalendarToDate(),
					as.get()
					);
			advisoryReasonDetailRepository.save(ardNew);
		}
				
		return new ResponseEntity<ApiResponseCustom>(
				new ApiResponseCustom(Instant.now(),200,"OK","new advisoryReason added",request.getRequestURI()
				), HttpStatus.OK);
	}
	
	@PutMapping("private/change-reason-name")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponseCustom> changeReasonName(@RequestParam long id, @RequestParam String name, HttpServletRequest request){
		
		Optional<AdvisoryReason> ar = advisoryReasonRepository.findById(id);
		if(!ar.isPresent()) {
			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(Instant.now(),200,"OK","Reason not found",request.getRequestURI()
					), HttpStatus.OK);
		}
		
		if(advisoryReasonRepository.findByAdvisoryReasonName(name).isPresent()) {
			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(Instant.now(),200,"OK","Reason name already present",request.getRequestURI()
					), HttpStatus.OK);
		}
		
		ar.get().setAdvisoryReasonName(name);
		advisoryReasonRepository.save(ar.get());
		
		return new ResponseEntity<ApiResponseCustom>(
				new ApiResponseCustom(Instant.now(),200,"OK","Reason name changed",request.getRequestURI()
				), HttpStatus.OK);
	}

	@GetMapping("private/get-advisory-detail")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponseCustom> getAdvisoryDetail(HttpServletRequest request){
		
		List<AdvisoryReasonResponse> ar = advisoryReasonDetailRepository.getAdvisoryReason(advisoryService.fromCalendarToDate());
		if(ar.isEmpty())
			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(Instant.now(),200,"OK", "No advisory reasons found" ,request.getRequestURI()
					), HttpStatus.OK);
		
		return new ResponseEntity<ApiResponseCustom>(
				new ApiResponseCustom(Instant.now(),200,"OK", ar ,request.getRequestURI()
				), HttpStatus.OK);
		
	}
	
}
