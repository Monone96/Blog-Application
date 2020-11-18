package it.course.myblogc3.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.course.myblogc3.entity.AdvisoryReason;

@Repository
public interface AdvisoryReasonRepository extends JpaRepository<AdvisoryReason, Long>{
	
	Optional<AdvisoryReason> findByAdvisoryReasonName(String name);
	

}
