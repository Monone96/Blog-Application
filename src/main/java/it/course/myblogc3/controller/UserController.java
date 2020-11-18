package it.course.myblogc3.controller;

import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import it.course.myblogc3.entity.Authority;
import it.course.myblogc3.entity.AuthorityName;
import it.course.myblogc3.entity.LoginAttempt;
import it.course.myblogc3.entity.LoginAttemptId;
import it.course.myblogc3.entity.Token;
import it.course.myblogc3.entity.User;
import it.course.myblogc3.payload.request.ChangeAuthoritiesRequest;
import it.course.myblogc3.payload.request.SignInRequest;
import it.course.myblogc3.payload.request.SignUpRequest;
import it.course.myblogc3.payload.request.UpdateMeRequest;
import it.course.myblogc3.payload.response.ApiResponseCustom;
import it.course.myblogc3.payload.response.UserResponse;
import it.course.myblogc3.repository.AuthorityRepository;
import it.course.myblogc3.repository.LoginAttemptRepository;
import it.course.myblogc3.repository.TokenRepository;
import it.course.myblogc3.repository.UserRepository;
import it.course.myblogc3.security.JwtAuthenticationResponse;
import it.course.myblogc3.security.JwtTokenUtil;
import it.course.myblogc3.service.UserService;


@RestController
public class UserController {
	
	@Autowired
	UserRepository userRepository;
	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	AuthenticationManager authenticationManager;
	@Autowired
	AuthorityRepository authorityRepository;
	@Autowired
	UserDetailsService userDetailsService;
	@Autowired
	JwtTokenUtil jwtTokenUtil;
	@Autowired
	UserService userService;
	@Autowired
	TokenRepository tokenRepository;
	@Autowired
	LoginAttemptRepository loginAttemptRepository;
	
	@Value("${jwt.header}")
	private String tokenHeader; 
	
	@PostMapping("/public/signup")
	@Transactional
	public ResponseEntity<ApiResponseCustom> signUp(@Valid @RequestBody SignUpRequest signUpRequest, 
			HttpServletRequest request) {
		
		long countUsers = userRepository.count(); // select count(*) from user;
		
		if(userRepository.existsByEmail(signUpRequest.getEmail())) {
			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(
						Instant.now(), 200,	"OK", "User already registered", request.getRequestURI()
					), HttpStatus.OK);
		}
		
		if(userRepository.existsByUsername(signUpRequest.getUsername())) {
			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(
						Instant.now(), 200,	"OK", "Username already in use", request.getRequestURI()
					), HttpStatus.OK);
		}
		
		User user = new User(signUpRequest.getEmail(), signUpRequest.getUsername(), signUpRequest.getPassword());
		user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
				
		String registrationConfirmCode = null;
		try {
			registrationConfirmCode = userService.toHexString(userService.getSHA(Instant.now().toString()));
			user.setRegistrationConfirmCode(registrationConfirmCode);
		} catch (NoSuchAlgorithmException e) {
			e.getStackTrace();
		}
		
		userRepository.save(user);
		
		Optional<Authority> userAuthority = Optional.empty();
		if(countUsers > 0) {
			userAuthority = authorityRepository.findByName(AuthorityName.ROLE_READER);
		} else {
			userAuthority = authorityRepository.findByName(AuthorityName.ROLE_ADMIN);
		}
		user.setAuthorities(Collections.singleton(userAuthority.get()));
		
		return new ResponseEntity<ApiResponseCustom>(
				new ApiResponseCustom(
					Instant.now(), 200,	"OK", "Check your email: localhost:8081/public/confirm-registration/"+registrationConfirmCode,	request.getRequestURI()
				), HttpStatus.OK);
	}
	
	@PutMapping("public/confirm-registration/{registrationConfirmCode}")
	public ResponseEntity<ApiResponseCustom> confirmRegistration(@PathVariable("registrationConfirmCode") String registrationConfirmCode,
			@RequestParam String email, HttpServletRequest request){
		
		Optional<User> u = userRepository.findByRegistrationConfirmCodeAndEmail(registrationConfirmCode, email);
		if(!u.isPresent())
			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(
						Instant.now(),
						200,
						"OK",
						"User not found or user already confirmed the registration",
						request.getRequestURI()
					), HttpStatus.OK);
		u.get().setRegistrationConfirmCode(null);
		u.get().setEnabled(true);
		
		userRepository.save(u.get());
		
		return new ResponseEntity<ApiResponseCustom>(
				new ApiResponseCustom(
					Instant.now(),
					200,
					"OK",
					"Your registration has been confirmed",
					request.getRequestURI()
				), HttpStatus.OK);
		
		}
	
	@PostMapping("/public/signin")
	public ResponseEntity<ApiResponseCustom> signIn(@Valid @RequestBody SignInRequest signInRequest,
			HttpServletRequest request, HttpServletResponse response) 
			throws AuthenticationException, JsonProcessingException{
		
		Optional<User> u = userRepository.findByUsernameOrEmail(signInRequest.getUsernameOrEmail(), signInRequest.getUsernameOrEmail());
		Optional<LoginAttempt> la=loginAttemptRepository.findById(new LoginAttemptId(u.get()));
		User user=u.get();
		String password=signInRequest.getPassword();
	
		if(!u.isPresent())
			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(
						Instant.now(), 200,	"OK", "Please register yourself before the Sign In", request.getRequestURI()
					), HttpStatus.OK);
		
		//banned user
		if(userService.isBanned(user))
			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(
						Instant.now(), 200,	"OK", "your are banned ", request.getRequestURI()
					), HttpStatus.OK);
			
		// not valid password
		if(!userService.isValidPassword(user,password)) {
			if(userService.getAttempt(user,la,request)==3) 
				return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(
							Instant.now(), 401,	"Unauthorized", "you can retry after 15 minutes ", request.getRequestURI()
						), HttpStatus.UNAUTHORIZED);
			else
				return new ResponseEntity<ApiResponseCustom>(
						new ApiResponseCustom(
							Instant.now(), 401,	"Unauthorized", "Wrong credentials ", request.getRequestURI()
						), HttpStatus.UNAUTHORIZED);
		}else 
			if(la.isPresent())
			  loginAttemptRepository.delete(la.get());
			
		final Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(u.get().getUsername(), signInRequest.getPassword())
				);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		if(user.getAuthorities().contains(new Authority(3L, AuthorityName.ROLE_READER))) {
			if(!userService.isAlreadyLoggedToday(user)) {
				user.setCredits(user.getCredits()+1);
				userRepository.save(user);
			}
		}
		
		final UserDetails userDetails = userDetailsService.loadUserByUsername(u.get().getUsername());
		final String token = jwtTokenUtil.generateToken(userDetails);
		
		response.setHeader(tokenHeader, token);
		
		return new ResponseEntity<ApiResponseCustom>(
				new ApiResponseCustom(
					Instant.now(), 200,	"OK",
					new JwtAuthenticationResponse(userDetails.getUsername(), userDetails.getAuthorities(), token),	
					request.getRequestURI()
				), HttpStatus.OK);
		
	}
	
	@PutMapping("private/update-authorities")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponseCustom> updateAuthorities(@Valid @RequestBody ChangeAuthoritiesRequest changeAuthoritiesRequest, HttpServletRequest request){
		
		User loggedUser = userService.getAuthenticatedUser();
		
		Optional<User> u = userRepository.findByUsername(changeAuthoritiesRequest.getUsername());
		if(!u.isPresent()) 
			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(
						Instant.now(),
						200,
						"OK",
						"User " + changeAuthoritiesRequest.getUsername() + " not found",
						request.getRequestURI()
					), HttpStatus.OK);
			
		if(changeAuthoritiesRequest.getUsername().equals(loggedUser.getUsername()))
			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(
						Instant.now(),
						200,
						"OK",
						"You can't change your own authorities",
						request.getRequestURI()
					), HttpStatus.OK);
		
		Set<AuthorityName> authorityNames = changeAuthoritiesRequest.getAuthorityNames();
		if(authorityNames.isEmpty())
			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(
						Instant.now(),
						200,
						"OK",
						"No authority has been selected",
						request.getRequestURI()
					), HttpStatus.OK);
		
		Set<Authority> authorities = authorityRepository.findByNameIn(authorityNames);
		
		u.get().setAuthorities(authorities);
		
		userRepository.save(u.get());
		
		return new ResponseEntity<ApiResponseCustom>(
				new ApiResponseCustom(
					Instant.now(),
					200,
					"OK",
					"Authorities have been updated",
					request.getRequestURI()
				), HttpStatus.OK);
		
	}
	
	
	@PutMapping("private/change-password")
	public ResponseEntity<ApiResponseCustom> changePassword(@Valid @RequestParam String newPassword, HttpServletRequest request){
		
		User loggedUser = userService.getAuthenticatedUser();
		
		loggedUser.setPassword(passwordEncoder.encode(newPassword));
		
		userRepository.save(loggedUser);
		
		return new ResponseEntity<ApiResponseCustom>(
				new ApiResponseCustom(
					Instant.now(),
					200,
					"OK",
					"User password has been updated",
					request.getRequestURI()
				), HttpStatus.OK);
		
	}
	
	@PutMapping("public/reset-password-ic")
	public ResponseEntity<ApiResponseCustom> resetPasswordIc(@Valid @RequestParam String email, HttpServletRequest request){
		
		Optional<User> u = userRepository.findByEmail(email);
		
		if(!u.isPresent()) 
			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(
						Instant.now(),
						200,
						"OK",
						"User with " + email + " not found",
						request.getRequestURI()
					), HttpStatus.OK);
		
		String identifierCode = null;
		
		try {
			identifierCode = userService.toHexString(userService.getSHA(Instant.now().toString()));
			u.get().setIdentifierCode(identifierCode);
			userRepository.save(u.get());
		} catch (NoSuchAlgorithmException e) {
			e.getStackTrace();
		}
		
		return new ResponseEntity<ApiResponseCustom>(
				new ApiResponseCustom(
					Instant.now(),
					200,
					"OK",
					"Check your email: localhost:8081/reset-password-ic/" + identifierCode,
					request.getRequestURI()
				), HttpStatus.OK);
	}
	
	@PutMapping("public/reset-password/{idCode}")
	public ResponseEntity<ApiResponseCustom> resetPassword2(@PathVariable("idCode") String idCode,
			@RequestParam String newPassword, @RequestParam String email, HttpServletRequest request){
		Optional<User> user=userRepository.findByIdentifierCodeAndEmail(idCode, email);
		if(!user.isPresent())
			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(
							Instant.now(),200,"OK","identifierCode Not valid",request.getRequestURI() ),HttpStatus.OK);
		
		User u=user.get();
		u.setPassword(passwordEncoder.encode(newPassword));
		u.setIdentifierCode(null);
		userRepository.save(u);

		return new ResponseEntity<ApiResponseCustom>(
				new ApiResponseCustom(
						Instant.now(),200,"OK","User has been resetted",request.getRequestURI() ),HttpStatus.OK);
	}
	
	@PutMapping("private/update-me")
	public ResponseEntity<ApiResponseCustom> updateMe(@Valid @RequestBody UpdateMeRequest updateMeRequest, 
			HttpServletRequest request) {
		
		User loggedUser = userService.getAuthenticatedUser();
		
		Optional<User> findUByEmail = userRepository.findByEmail(updateMeRequest.getEmail());
		
		if(!findUByEmail.isPresent()) {
			loggedUser.setEmail(updateMeRequest.getEmail());
		}
		else if(findUByEmail.isPresent() && findUByEmail.get().equals(loggedUser)) {
				loggedUser.setEmail(updateMeRequest.getEmail());
		} else {
			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(
							Instant.now(),200,"OK","Please choose another email",request.getRequestURI() ),HttpStatus.OK);
		}
	
		Optional<User> findUByUsername = userRepository.findByUsername(updateMeRequest.getUsername());
		if(!findUByUsername.isPresent()) {
			loggedUser.setUsername(updateMeRequest.getUsername());
		}
		else if(findUByUsername.isPresent() && findUByUsername.get().getUsername().equals(loggedUser.getUsername())) {
				loggedUser.setUsername(updateMeRequest.getUsername());
		} else {
			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(
							Instant.now(),200,"OK","Please choose another username",request.getRequestURI() ),HttpStatus.OK);
		}
		
		userRepository.save(loggedUser);
			
		return new ResponseEntity<ApiResponseCustom>(
				new ApiResponseCustom(
						Instant.now(),200,"OK","Profile updated",request.getRequestURI() ),HttpStatus.OK);
		
	}
	
	@GetMapping("private/get-users")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponseCustom> getUsers(HttpServletRequest request){
		
		List<User> us = userRepository.findAll();
		
		List<UserResponse> usr = us.stream().map(ur -> new UserResponse(
				ur.getId(),
				ur.getUsername(),
				ur.getEmail(),
				ur.getEnabled(),
				ur.getAuthorities().stream().map(a -> a.getName()).collect(Collectors.toSet())
				)).collect(Collectors.toList());
		
		//id, username, email, enabled, authorities, 
		
		return new ResponseEntity<ApiResponseCustom>(
				new ApiResponseCustom(
					Instant.now(), 200, "OK", usr, request.getRequestURI()
					), HttpStatus.OK);
		
	}
	
	@GetMapping("private/get-user/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponseCustom> getUser(@PathVariable Long id, HttpServletRequest request){
		
		Optional<User> user = userRepository.findById(id);
		
		Optional<UserResponse> usr = user.map(ur -> new UserResponse(
				ur.getId(),
				ur.getUsername(),
				ur.getEmail(),
				ur.getEnabled(),
				ur.getAuthorities().stream().map(a -> a.getName()).collect(Collectors.toSet())
				));
		
		return new ResponseEntity<ApiResponseCustom>(
				new ApiResponseCustom(
					Instant.now(), 200, "OK", usr, request.getRequestURI()
					), HttpStatus.OK);
		//return UserResponse
		
	}
	
	@PostMapping("/private/logout")
	public ResponseEntity<ApiResponseCustom> logout(HttpServletRequest request){
		
		String authToken = request.getHeader(this.tokenHeader);
		Token t = new Token(authToken, jwtTokenUtil.getExpirationDateFromToken(authToken));
		tokenRepository.save(t);
		
		return new ResponseEntity<ApiResponseCustom>(
				new ApiResponseCustom(
					Instant.now(), 200, "OK", "Logout", request.getRequestURI()
					), HttpStatus.OK);
		
	}
	
	@DeleteMapping("/private/clean-expired-tokens")
	@PreAuthorize("hasRole('ADMIN')")
	@Transactional
	public ResponseEntity<ApiResponseCustom> cleanExpiredTokens(HttpServletRequest request){

		tokenRepository.deleteAllByExpiryDateLessThan(new Date());
		
		return new ResponseEntity<ApiResponseCustom>(
			new ApiResponseCustom(
					Instant.now(), 200, "OK", "Expired tokens have been removed", request.getRequestURI()
					), HttpStatus.OK);
		
	}
	
	@GetMapping("public/ip")
	public ResponseEntity<ApiResponseCustom> getIP(HttpServletRequest request){
		
		return new ResponseEntity<ApiResponseCustom>(new ApiResponseCustom(Instant.now(), 200, 
				"OK", request.getRemoteAddr(), request.getRequestURI()), HttpStatus.OK);
	}
	
}
		
		
		
			
		
