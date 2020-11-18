package it.course.myblogc3.controller;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import it.course.myblogc3.entity.Advisory;
import it.course.myblogc3.entity.AdvisoryId;
import it.course.myblogc3.entity.AdvisoryReason;
import it.course.myblogc3.entity.AdvisoryStatus;
import it.course.myblogc3.entity.Comment;
import it.course.myblogc3.entity.User;
import it.course.myblogc3.payload.request.AdvisoryRequest;
import it.course.myblogc3.payload.request.AdvisoryStatusUpdateRequest;
import it.course.myblogc3.payload.response.AdvisoryResponse;
import it.course.myblogc3.payload.response.ApiResponseCustom;
import it.course.myblogc3.repository.AdvisoryReasonDetailRepository;
import it.course.myblogc3.repository.AdvisoryReasonRepository;
import it.course.myblogc3.repository.AdvisoryRepository;
import it.course.myblogc3.repository.AdvisorySeverityRepository;
import it.course.myblogc3.repository.CommentRepository;
import it.course.myblogc3.repository.UserRepository;
import it.course.myblogc3.service.AdvisoryService;
import it.course.myblogc3.service.BanService;
import it.course.myblogc3.service.UserService;

@RestController
public class AdvisoryController {
	
	@Autowired
	UserService userService;
	@Autowired
	CommentRepository commentRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	AdvisoryReasonRepository advisoryReasonRepository;
	@Autowired
	AdvisoryRepository advisoryRepository;
	@Autowired
	AdvisorySeverityRepository advisorySeverityRepository;
	@Autowired
	BanService banService;
	@Autowired
	AdvisoryService advisoryService;
	@Autowired
	AdvisoryReasonDetailRepository advisoryReasonDetailRepository;
 
	@PostMapping("private/create-advisory")
	@PreAuthorize("hasRole('EDITOR') or hasRole('READER')")
	public ResponseEntity<ApiResponseCustom> createAdvisory(@Valid @RequestBody AdvisoryRequest advisoryRequest, HttpServletRequest request){
	
		Optional<Comment> c = commentRepository.findByIdAndVisibleTrue(advisoryRequest.getCommentId());
		if(!c.isPresent()) {
			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(
						Instant.now(), 404, "NOT FOUND", "Comment not found", request.getRequestURI()
					), HttpStatus.NOT_FOUND);
		}
		User u = userService.getAuthenticatedUser();
		if(c.get().getCommentAuthor().getId().equals(u.getId())){
			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(
						Instant.now(), 401, "Forbidden", "Why are you reporting yourself?", request.getRequestURI()
					), HttpStatus.FORBIDDEN);
		}
		Optional<AdvisoryReason> ar = advisoryReasonRepository.findByAdvisoryReasonName(advisoryRequest.getReason());
		if(!ar.isPresent()) {
			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(
						Instant.now(), 404, "NOT FOUND", "Reason not found", request.getRequestURI()
					), HttpStatus.NOT_FOUND);
		}
		
		AdvisoryId aid = new AdvisoryId(c.get(), userService.getAuthenticatedUser(), ar.get());
		
		boolean exists = advisoryRepository.existsByAdvisoryIdCommentAndAdvisoryIdAdvisoryReason(c.get(), ar.get());                                                                                                                                                     
		if(exists) {
			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(
						Instant.now(), 404, "Forbidden", "This comment has been already reported for the same reason", request.getRequestURI()
					), HttpStatus.FORBIDDEN);
		}
		
		Advisory a = new Advisory(aid, AdvisoryStatus.OPEN, advisoryRequest.getDescription());
		
		advisoryRepository.save(a);
		
		return new ResponseEntity<ApiResponseCustom>(
				new ApiResponseCustom(
					Instant.now(), 200, "OK", "New advisory added", request.getRequestURI()
				), HttpStatus.OK);
		
	}
	
	@PutMapping("private/change-advisory-status")
	@PreAuthorize("hasRole('ADMIN')")
	@Transactional
	public ResponseEntity<ApiResponseCustom> changeAdvisoryStatus(@Valid @RequestBody AdvisoryStatusUpdateRequest advisoryStatusUpdateRequest, HttpServletRequest request){
		
		Advisory a = advisoryRepository.getAdvisoryById(
				advisoryStatusUpdateRequest.getUserId(),
				advisoryStatusUpdateRequest.getCommentId(),
				advisoryStatusUpdateRequest.getReasonId()
				);
				
		if(a == null)
			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(
						Instant.now(), 404, "NOT FOUND", "Advisory not found", request.getRequestURI()
					), HttpStatus.NOT_FOUND);
		
		if(a.getStatus().ordinal() == 2 || a.getStatus().ordinal() == 3)
			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(Instant.now(),404,"NOT FOUND","Advisory alredy closed",request.getRequestURI()
					), HttpStatus.NOT_FOUND);
		
		a.setStatus(AdvisoryStatus.valueOf(advisoryStatusUpdateRequest.getStatus()));
		
		if(advisoryStatusUpdateRequest.getStatus().equals(AdvisoryStatus.valueOf("CLOSED_WITH_CONSEQUENCE").toString() )) {
			commentRepository.updateVisibilityComment(advisoryStatusUpdateRequest.getCommentId());

			int severity = advisoryReasonDetailRepository.getSeverityValue(advisoryStatusUpdateRequest.getReasonId());
			
			Optional<User> u = userRepository.getCommentAuthorByCommentId(advisoryStatusUpdateRequest.getCommentId());
			
			banService.updateBannedUntil(u.get(), severity);
		}
		
		//advisoryRepository.save(a);
		
		return new ResponseEntity<ApiResponseCustom>(
				new ApiResponseCustom(
					Instant.now(), 200, "OK", "Advisory status has been updated", request.getRequestURI()
				), HttpStatus.OK);
			
	}
	
	@GetMapping("private/get-open-advisories")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponseCustom> getOpenAdvisories(HttpServletRequest request){
		
		List<AdvisoryResponse> lar = advisoryRepository.getOpenAdvisories();
		if(lar.isEmpty()) {
			return new ResponseEntity<ApiResponseCustom>(
				new ApiResponseCustom(
					Instant.now(), 404, "NOT FOUND", "No open advisories found", request.getRequestURI()
				), HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<ApiResponseCustom>(
				new ApiResponseCustom(
					Instant.now(), 200, "OK", lar, request.getRequestURI()
				), HttpStatus.OK);
		
	}
	
	
}
