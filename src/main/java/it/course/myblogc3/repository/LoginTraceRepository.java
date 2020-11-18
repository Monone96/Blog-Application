package it.course.myblogc3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.course.myblogc3.entity.LoginTrace;
import it.course.myblogc3.entity.LoginTraceId;

@Repository
public interface LoginTraceRepository extends JpaRepository<LoginTrace, LoginTraceId> {

	
	
}
