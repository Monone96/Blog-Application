package it.course.myblogc3.controller;

import java.time.Instant;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import it.course.myblogc3.entity.Comment;
import it.course.myblogc3.entity.Post;
import it.course.myblogc3.entity.User;
import it.course.myblogc3.payload.request.CommentRequest;
import it.course.myblogc3.payload.response.ApiResponseCustom;
import it.course.myblogc3.payload.response.CommentResponse;
import it.course.myblogc3.repository.CommentRepository;
import it.course.myblogc3.repository.PostRepository;
import it.course.myblogc3.service.UserService;

@RestController
public class CommentController {
	
	@Autowired
	PostRepository postRepository;
	@Autowired
	CommentRepository commentRepository;
	@Autowired
	UserService userService;
	
	//create comment passing comment text
	@PostMapping("private/create-comment/{postId}")
	@PreAuthorize("hasRole('READER') or hasRole('EDITOR')")
	@Transactional
	public ResponseEntity<ApiResponseCustom> createComment(@PathVariable Long postId, @Valid @RequestBody CommentRequest commentRequest, HttpServletRequest request){
		
		//Post p = postRepository.findOneWithAuthorById(postId);
		boolean exists = postRepository.existsByIdAndVisibleTrue(postId);

		if(!exists){
			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(
						Instant.now(), 200, "OK", "Post not found" , request.getRequestURI()
					), HttpStatus.OK);
		}
		User u = userService.getAuthenticatedUser();
		Comment c = new Comment(commentRequest.getComment(), new Post(postId), u);
		commentRepository.save(c);
		
		return new ResponseEntity<ApiResponseCustom>(
				new ApiResponseCustom(
					Instant.now(), 200, "OK", "New comment has been added to post " + postId, request.getRequestURI()
				), HttpStatus.OK);
	}
	
	@GetMapping("public/get-comment/{commentId}")
	public ResponseEntity<ApiResponseCustom> getComment(@PathVariable Long commentId, HttpServletRequest request){
		
		CommentResponse c = commentRepository.getCommentResponse(commentId);
		if(c == null)
			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(
						Instant.now(), 200, "OK", "Comment not found" , request.getRequestURI()
					), HttpStatus.OK);
		
		return new ResponseEntity<ApiResponseCustom>(
				new ApiResponseCustom(
					Instant.now(), 200, "OK", c, request.getRequestURI()
				), HttpStatus.OK);
		
	}
	
	@DeleteMapping("private/delete-comment/{commentId}")
	public ResponseEntity<ApiResponseCustom> deleteComment(@PathVariable Long commentId, HttpServletRequest request){
		
		commentRepository.deleteComment(commentId);

		return new ResponseEntity<ApiResponseCustom>(
				new ApiResponseCustom(
					Instant.now(), 200, "OK", "The comment with id " + commentId + " has been deleted", request.getRequestURI()
				), HttpStatus.OK);
		
	}

}
