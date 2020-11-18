package it.course.myblogc3.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import it.course.myblogc3.entity.Authority;
import it.course.myblogc3.entity.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	
	Optional<User> findByEmail(String email);	
	
	Optional<User> findByUsernameOrEmail(String username, String email);
	
	Boolean existsByUsernameOrEmail(String username, String email);
	
	List<User> findAllByEnabledTrue();
	List<User> findAllByAuthoritiesIn(Set<Authority> sa);
	
	Optional<User> findByIdAndEnabledTrue(Long id);
	
	Optional<User> findByUsername(String username);
	Optional<User> findByUsernameAndEnabledTrue(String username);
    
    Boolean existsByUsername(String username);
	Boolean existsByEmail(String email);
	
	Optional<User> findByIdentifierCode(String identifierCode);
	
	Optional<User> findByIdentifierCodeAndEmail(String identifierCode, String email);
	
	Optional<User> findByRegistrationConfirmCodeAndEmail(String registrationConfirmCode, String email);
	
	@Query(value="SELECT user.id FROM myblog.user RIGHT OUTER JOIN myblog.post ON myblog.user.id = myblog.post.author GROUP BY user.id", nativeQuery=true)
	List<Long> findUserIdFromUserWhoWrotePosts();
	
	@Query(value="SELECT c.commentAuthor FROM Comment c "
			+ "WHERE c.id = :commentId ")
	Optional<User> getCommentAuthorByCommentId(long commentId);

	@Query(value="SELECT p.author.username "
			+ "FROM Post p "
			+ "WHERE p.id = :postId ")
	String getAuthorUsernameByPostId(long postId);
	
}
