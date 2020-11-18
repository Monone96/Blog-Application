package it.course.myblogc3.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.course.myblogc3.entity.AdvisorySeverity;

@Repository
public interface AdvisorySeverityRepository extends JpaRepository<AdvisorySeverity, String> {

	List<AdvisorySeverity> findAllByOrderBySeverityValueDesc();
	
}
