package it.course.myblogc3.entity;

import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.JoinColumn;

import org.hibernate.annotations.NaturalId;

import it.course.myblogc3.entity.audit.DateAudit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.NoArgsConstructor;

@Entity
@Table(name="USER")
@Getter @Setter @ToString @AllArgsConstructor @NoArgsConstructor
public class User extends DateAudit{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@NaturalId(mutable=true)
	@Column(name="EMAIL", nullable=false, length=120)
	private String email;
	
	@Column(name="USERNAME", unique=true, nullable=false, length=12)
	private String username;
	
	@Column(name="PASSWORD", nullable=false, length=100)
	private String password;
	
	@Column(name="IS_ENABLED", columnDefinition="TINYINT(1)")
	private Boolean enabled = false;
	
	private String identifierCode;
	
	private String registrationConfirmCode;
	
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name="USER_AUTHORITIES",
			joinColumns = {@JoinColumn(name="USER_ID", referencedColumnName="ID")},
			inverseJoinColumns = {@JoinColumn(name="AUTHORITY_ID", referencedColumnName="ID")})
	private Set<Authority> authorities;
	
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name="USER_PREFERRED_TAGS",
			joinColumns = {@JoinColumn(name="USER_ID", referencedColumnName="ID")},
			inverseJoinColumns = {@JoinColumn(name="TAG_ID", referencedColumnName="TAG_NAME")})
	private Set<Tag> preferredTags;
	
	private LocalDateTime bannedUntil;

	@Column(nullable=false, columnDefinition="INT(6)")
	private int credits = 0;
	
	public User(String email, String username, String password) {
		super();
		this.email = email;
		this.username = username;
		this.password = password;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((authorities == null) ? 0 : authorities.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((enabled == null) ? 0 : enabled.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (authorities == null) {
			if (other.authorities != null)
				return false;
		} else if (!authorities.equals(other.authorities))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (enabled == null) {
			if (other.enabled != null)
				return false;
		} else if (!enabled.equals(other.enabled))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	public User(Long id) {
		super();
		this.id = id;
	}
	
	
}
