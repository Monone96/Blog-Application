package it.course.myblogc3.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.course.myblogc3.entity.AdvisoryReason;
import it.course.myblogc3.entity.AdvisoryReasonDetail;
import it.course.myblogc3.entity.AdvisoryReasonDetailId;
import it.course.myblogc3.payload.response.AdvisoryReasonResponse;

@Repository
public interface AdvisoryReasonDetailRepository extends JpaRepository<AdvisoryReasonDetail, AdvisoryReasonDetailId>{
	
	Optional<AdvisoryReasonDetail> findByAdvisoryReasonDetailIdAdvisoryReasonAndEndDateEquals(AdvisoryReason advisoryReason, Date endDate);

	@Query(value="SELECT new it.course.myblogc3.payload.response.AdvisoryReasonResponse"
			+ "("
			+ "a.advisoryReasonDetailId.startDate, "
			+ "a.endDate, "
			+ "a.advisorySeverity.severityDescription, "
			+ "a.advisorySeverity.severityValue, "
			+ "a.advisoryReasonDetailId.advisoryReason.id, "
			+ "a.advisoryReasonDetailId.advisoryReason.advisoryReasonName"
			+ ") "
			+ "FROM AdvisoryReasonDetail a "
			+ "WHERE a.endDate = :endDate "
			+ "ORDER BY a.advisorySeverity.severityValue DESC, a.advisoryReasonDetailId.advisoryReason.advisoryReasonName ASC"
			)
	List<AdvisoryReasonResponse> getAdvisoryReason(@Param("endDate") Date endDate);
	
	@Query(value="SELECT ard.advisorySeverity.severityValue "
			+ "FROM Advisory a "
			+ "LEFT JOIN AdvisoryReasonDetail ard "
			+ "ON a.advisoryId.advisoryReason.id = ard.advisoryReasonDetailId.advisoryReason.id "
			+ "WHERE ard.advisoryReasonDetailId.advisoryReason.id = :reasonId "
			+ "AND a.createdAt BETWEEN ard.advisoryReasonDetailId.startDate AND ard.endDate ")
	int getSeverityValue(@Param ("reasonId") long reasonId);

	
}
