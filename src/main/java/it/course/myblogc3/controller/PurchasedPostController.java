package it.course.myblogc3.controller;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import it.course.myblogc3.entity.Post;
import it.course.myblogc3.entity.PurchasedPost;
import it.course.myblogc3.entity.PurchasedPostId;
import it.course.myblogc3.entity.User;
import it.course.myblogc3.payload.response.ApiResponseCustom;
import it.course.myblogc3.repository.PostCostRepository;
import it.course.myblogc3.repository.PostRepository;
import it.course.myblogc3.repository.PurchasedPostRepository;
import it.course.myblogc3.repository.UserRepository;
import it.course.myblogc3.service.PostService;
import it.course.myblogc3.service.UserService;

@RestController
public class PurchasedPostController {

	@Autowired PurchasedPostRepository purchasedPostRepository;
	@Autowired UserService userService;
	@Autowired UserRepository userRepository;
	@Autowired PostRepository postRepository;
	@Autowired PostCostRepository postCostRepository;
	@Autowired PostService postService;
	
	@PostMapping("private/purchase-post/{postId}")
	@PreAuthorize("hasRole('READER')")
	@Transactional
	public ResponseEntity<ApiResponseCustom> purchasePost(@PathVariable long postId, HttpServletRequest request){
		
		User u = userService.getAuthenticatedUser();
		
		int userCredits = u.getCredits();
		
		Optional<Post> p = postRepository.findByIdAndVisibleTrue(postId);
		if(!p.isPresent()) {
			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(Instant.now(), 200, "Not Found", "Post " + postId + " not found", request.getRequestURI()),HttpStatus.NOT_FOUND);
		}
		
		int actualCost = postService.getActualRealCost(postId, p.get().getCost());
		
		if(p.get().getCost() == 0) {
			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(Instant.now(), 200, "OK", "Post " + postId + " is free", request.getRequestURI()),HttpStatus.OK);
		}
		
		if(userCredits < actualCost) {
			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(Instant.now(), 200, "Unauthorized", "You have not enough credits", request.getRequestURI()),HttpStatus.UNAUTHORIZED);
		}else {
			PurchasedPostId purchasedPostId = new PurchasedPostId(p.get(), u);
			if(purchasedPostRepository.existsById(purchasedPostId)) {
				return new ResponseEntity<ApiResponseCustom>(
						new ApiResponseCustom(Instant.now(), 200, "OK", "You have already purchased this post", request.getRequestURI()),HttpStatus.OK);
			}
			PurchasedPost purchasedPost = new PurchasedPost(purchasedPostId);
			u.setCredits(u.getCredits() - actualCost);
			purchasedPostRepository.save(purchasedPost);
		}
		
		return new ResponseEntity<ApiResponseCustom>(
				new ApiResponseCustom(Instant.now(), 200, "OK", "Post " + postId + " purchased", request.getRequestURI()),HttpStatus.OK);
	}
}
