package it.course.myblogc3.controller;

import java.time.Instant;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.course.myblogc3.entity.Tag;
import it.course.myblogc3.entity.User;
import it.course.myblogc3.payload.response.ApiResponseCustom;
import it.course.myblogc3.repository.TagRepository;
import it.course.myblogc3.repository.UserRepository;
import it.course.myblogc3.service.UserService;

@RestController
public class TagController {
	
	@Autowired
	TagRepository tagRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	UserService userService;

	@PutMapping("private/change-tag-visibility")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponseCustom> changeTagVisibility(@RequestParam String tagName, HttpServletRequest request){
		
		Optional<Tag> t = tagRepository.findById(tagName);
		if(!t.isPresent())
			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(
						Instant.now(), 200, "OK", "No tag " + tagName + " found", request.getRequestURI()
						), HttpStatus.OK);
		
		t.get().setVisible(!t.get().getVisible());
		tagRepository.save(t.get());
		
		return new ResponseEntity<ApiResponseCustom>(
				new ApiResponseCustom(
					Instant.now(), 200, "OK", "Tag " + tagName + " has been updated", request.getRequestURI()
					), HttpStatus.OK);
		
	}
	
	@PostMapping("private/add-preferred-tag")
	@PreAuthorize("hasRole('READER') or hasRole('EDITOR')")
	@Transactional
	public ResponseEntity<ApiResponseCustom> addPreferredTag(@RequestParam String tagName, HttpServletRequest request){
		
		Optional<Tag> tag = tagRepository.findByTagNameAndVisibleTrue(tagName.toUpperCase());
		if(!tag.isPresent())
			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(
						Instant.now(), 200, "OK", "Tag not present", request.getRequestURI()
						), HttpStatus.OK);
		
		User user = userService.getAuthenticatedUser();
		if(user.getPreferredTags().contains(tag.get()))
			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(
						Instant.now(), 200, "OK", "Tag already present into your preferred", request.getRequestURI()
						), HttpStatus.OK);
		
		user.getPreferredTags().add(tag.get());
		
		return new ResponseEntity<ApiResponseCustom>(
				new ApiResponseCustom(
					Instant.now(), 200, "OK", "Tag " + tagName + " has been added into your preferred", request.getRequestURI()
					), HttpStatus.OK);
		
	}
	
}
