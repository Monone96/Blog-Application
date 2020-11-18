package it.course.myblogc3.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import it.course.myblogc3.entity.Language;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="LANGUAGE")
@Data @AllArgsConstructor @NoArgsConstructor
public class Language {

	@Id
	@Column(name="LANG_CODE", columnDefinition="VARCHAR(2)")
	private String langCode;
	
	@Column(name="LANG_DESC", nullable=false, unique=true, columnDefinition="VARCHAR(15)")
	private String langDesc;
	
	@Column(name="IS_VISIBLE")
	private Boolean visible = true;

	public Language(String langCode, String langDesc) {
		super();
		this.langCode = langCode;
		this.langDesc = langDesc;
	}
	
}
