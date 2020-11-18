package it.course.myblogc3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.course.myblogc3.entity.LoginAttempt;
import it.course.myblogc3.entity.LoginAttemptId;

@Repository
public interface LoginAttemptRepository extends JpaRepository<LoginAttempt, LoginAttemptId> {

	
	
}
