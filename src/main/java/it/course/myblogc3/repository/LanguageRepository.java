package it.course.myblogc3.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.course.myblogc3.entity.Language;

@Repository
public interface LanguageRepository extends JpaRepository<Language, String> {

	boolean existsByLangCodeOrLangDesc(String langCode, String langDesc);
	
	Optional<Language> findByLangCodeAndVisibleTrue(String langCode);
	
	List<Language> findAllByVisibleTrue();
	

}
