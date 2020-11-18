package it.course.myblogc3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.course.myblogc3.entity.PurchasedPost;
import it.course.myblogc3.entity.PurchasedPostId;

@Repository
public interface PurchasedPostRepository extends JpaRepository<PurchasedPost, PurchasedPostId> {
	
	
	
}
