package it.course.myblogc3.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.course.myblogc3.entity.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, String> {

	@Query(value="SELECT p.tags FROM Post p WHERE p.id=:postId")
	Set<Tag> getTagsInPost(@Param("postId") long postId);
	
	List<Tag> findAllByVisibleTrue();
	
	Set<Tag> findByTagNameInAndVisibleTrue(Set<String> tagNames);
	
	Optional<Tag> findByTagNameAndVisibleTrue(String tagName);
	
}
