package it.course.myblogc3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import it.course.myblogc3.entity.Voting;
import it.course.myblogc3.entity.VotingId;

@Repository
public interface VotingRepository extends JpaRepository<Voting, VotingId> {
	
	@Query(value="select AVG(v.vote) FROM Voting v RIGHT OUTER JOIN Post p "
			+ "ON p.id = v.votingId.post.id WHERE p.visible=true AND p.id=:postId")
	Double findAvgById(@Param("postId") Long postId);
	
	@Transactional
	@Modifying
	@Query(value="INSERT INTO Voting "
			+ "(created_at, vote, voter, post_id) "
			+ "VALUES (CURRENT_TIMESTAMP, :vote, :voter, :postId)", nativeQuery=true
			)
	void insertVote (@Param("vote") int vote, @Param("voter") long voter, @Param("postId") long postId);
	
	@Transactional
	@Modifying
	@Query(value="UPDATE Voting "
			+ "SET vote=:vote "
			+ "WHERE voter=:voter AND post_id=:postId", nativeQuery=true
			)
	void updateVote (@Param("vote") int vote, @Param("voter") long voter, @Param("postId") long postId);
	
	/*
	@Query(value="select new it.course.myblog.payload.response.PostResponseWithVoting"
			+ "("
			+ "p.id, "
			+ "p.title, "
			+ "p.content, "
			+ "p.language.langCode, "
			+ "p.dbFile.fileName, "
			+ "p.author.username, "		
			+ "p.updatedAt, "	
			+ "COUNT(v.vote), "
			+ "COALESCE(AVG(v.vote), 0.00) AS average"
			+ ")"
			+ "FROM Voting v "
			+ "RIGHT OUTER JOIN "
			+ "Post p "
			+ "ON p.id = v.votingId.post.id "
			+ "WHERE p.visible=true "
			+ "GROUP BY p.title "
			+ "ORDER BY average desc "
		)
	List<PostResponseWithVoting> findPostCoutAndAvg();
	
	@Query(value="select new it.course.myblog.payload.response.PostResponseWithVoting"
			+ "("
			+ "p.id, "
			+ "p.title, "
			+ "p.content, "
			+ "p.language.langCode, "
			+ "p.dbFile.fileName, "
			+ "p.author.username, "		
			+ "p.updatedAt, "	
			+ "COUNT(v.vote), "
			+ "COALESCE(AVG(v.vote), 0.00) AS average"
			+ ")"
			+ "FROM Voting v "
			+ "RIGHT OUTER JOIN "
			+ "Post p "
			+ "ON p.id = v.votingId.post.id "
			+ "WHERE p.visible=true AND p.author.id=:authorId "
			+ "GROUP BY p.title "
			+ "ORDER BY average desc "
		)
	List<PostResponseWithVoting> findPostCountAndAvgByAuthor(@Param("authorId") Long authorId);
	
	@Query(value="select new it.course.myblog.payload.response.AuthorResponse"
			+ "("
			+ "p.author.id, "
			+ "p.author.username, "
			+ "p.author.email, "
			+ "COUNT(v.vote), "
			+ "COALESCE(AVG(v.vote), 0.00) AS average"
			+ ")"
			+ "FROM Voting v "
			+ "RIGHT OUTER JOIN "
			+ "Post p "
			+ "ON p.id = v.votingId.post.id "
			+ "WHERE p.visible=true AND p.author.id=:authorId"
			)
	AuthorResponse findAuthorCountAndAvg(@Param("authorId") Long authorId);
	*/
	
	
}
