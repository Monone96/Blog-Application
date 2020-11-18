package it.course.myblogc3.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.course.myblogc3.entity.PostCost;
import it.course.myblogc3.entity.PostCostId;

@Repository
public interface PostCostRepository extends JpaRepository<PostCost, PostCostId> {

	@Query(value="SELECT pc "
			+ "FROM PostCost pc "
			+ "WHERE pc.postCostId.post.id = :postId "
			+ "AND (:endDate > pc.postCostId.startDate OR :endDate = pc.postCostId.startDate) "
			+ "AND (:startDate < pc.postCostId.endDate OR :startDate = pc.postCostId.endDate) "
			)
	List<PostCost> findActivePostCostByPostIdAndDates(@Param("postId")long postId, 
			@Param("startDate") Date startDate, @Param("endDate") Date endDate );
	
	@Query(value="SELECT pc "
			+ "FROM PostCost pc "
			+ "WHERE pc.postCostId.post.id=:postId "
			+ "AND CURRENT_TIMESTAMP BETWEEN pc.postCostId.startDate "
			+ "AND pc.postCostId.endDate ")
	List<PostCost> findActivePostCostByPostId(@Param("postId") long postId);
	
}


