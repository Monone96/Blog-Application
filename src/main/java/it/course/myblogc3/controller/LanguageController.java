package it.course.myblogc3.controller;

import java.time.Instant;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import it.course.myblogc3.entity.Language;
import it.course.myblogc3.payload.request.LanguageRequest;
import it.course.myblogc3.payload.response.ApiResponseCustom;
import it.course.myblogc3.repository.LanguageRepository;

@Validated
@RestController
public class LanguageController {

	@Autowired LanguageRepository languageRepository;
	
	@PostMapping("private/create-lang")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponseCustom> createLang(@Valid @RequestBody LanguageRequest languageRequest, HttpServletRequest request){
		
		boolean exists = languageRepository.existsByLangCodeOrLangDesc(languageRequest.getLangCode(), languageRequest.getLangDesc());
		
		if(exists)
			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(
						Instant.now(), 200, "OK", "Language already present", request.getRequestURI()
					), HttpStatus.OK);
		
		Language l = new Language(languageRequest.getLangCode().toUpperCase(), languageRequest.getLangDesc().toUpperCase());		
		languageRepository.save(l);
		
		return new ResponseEntity<ApiResponseCustom>(
				new ApiResponseCustom(
					Instant.now(), 200, "OK", "New language has been added: " + l.toString(), request.getRequestURI()
				), HttpStatus.OK);
		
	}
	
	@PutMapping("private/update-lang")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponseCustom> updateLang(@Valid @RequestBody LanguageRequest langRequest, HttpServletRequest request){
		
		Optional<Language> l = languageRepository.findById(langRequest.getLangCode());
		if(!l.isPresent()) 
			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(
						Instant.now(), 204, "OK", "No Language found.", request.getRequestURI()
					), HttpStatus.OK);
		
		
		l.get().setLangDesc(langRequest.getLangDesc());
		languageRepository.save(l.get());
		 
		return new ResponseEntity<ApiResponseCustom>(
				new ApiResponseCustom(
					Instant.now(), 200, "OK", "Language " + langRequest.getLangCode() + " successfully updated.", request.getRequestURI()
				), HttpStatus.OK);
	
	}
	
	@PutMapping("private/change-status-lang/{langCode}") 
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponseCustom> changeStatusLang(@PathVariable @NotBlank @Size(min=2, max=2) String langCode ,HttpServletRequest request){
		
		Optional<Language> l = languageRepository.findById(langCode);
		
		if(!l.isPresent()) 
			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(
						Instant.now(), 204, "OK", "No Language found.", request.getRequestURI()
					), HttpStatus.OK);
		
			
		l.get().setVisible(!l.get().getVisible());
		languageRepository.save(l.get());
		
		return new ResponseEntity<ApiResponseCustom>(
				new ApiResponseCustom(
					Instant.now(), 200, "OK", "Language " + langCode + " successfully updated.", request.getRequestURI()
				), HttpStatus.OK);
		
	}
}
	

