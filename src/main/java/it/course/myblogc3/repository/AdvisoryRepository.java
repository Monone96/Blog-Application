package it.course.myblogc3.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.course.myblogc3.entity.Advisory;
import it.course.myblogc3.entity.AdvisoryId;
import it.course.myblogc3.entity.AdvisoryReason;
import it.course.myblogc3.entity.Comment;
import it.course.myblogc3.payload.response.AdvisoryResponse;

@Repository
public interface AdvisoryRepository extends JpaRepository<Advisory, AdvisoryId> {
	
	@Query(value="SELECT * FROM advisory WHERE reporter=:reporterId "
			+ "AND comment_id=:commentId AND advisory_reason=:reasonId", nativeQuery=true)
	Advisory getAdvisoryById(@Param("reporterId") long reporterId,@Param("commentId") long commentId, @Param("reasonId") long reasonId);

	@Query(value="SELECT new it.course.myblogc3.payload.response.AdvisoryResponse"
			+ "("
			+ "ad.createdAt, "
			+ "ad.updatedAt, "
			+ "a.advisorySeverity.severityDescription, "
			+ "a.advisorySeverity.severityValue, "
			+ "a.advisoryReasonDetailId.advisoryReason.id, "
			+ "a.advisoryReasonDetailId.advisoryReason.advisoryReasonName, "
			+ "ad.status, "
			+ "ad.advisoryId.reporter.username, "
			+ "ad.advisoryId.comment.commentAuthor.username, "
			+ "ad.advisoryId.comment.id"
			+ ") "
			+ "FROM Advisory ad "
			+ "LEFT JOIN AdvisoryReasonDetail a "
			+ "ON ad.advisoryId.advisoryReason.id = a.advisoryReasonDetailId.advisoryReason.id "
			+ "WHERE ad.status IN (0,1) "
			+ "AND ad.createdAt BETWEEN a.advisoryReasonDetailId.startDate AND a.endDate "
			+ "ORDER BY ad.status ASC"
			)
	List<AdvisoryResponse> getOpenAdvisories();
	
	boolean existsByAdvisoryIdCommentAndAdvisoryIdAdvisoryReason(Comment c, AdvisoryReason ar);
}
