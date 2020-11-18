package it.course.myblogc3.security;

import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Collection;

public class JwtAuthenticationResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private final String username;
    Collection<? extends GrantedAuthority> authorities;

    private final String token;
  

    public String getUsername() {
        return this.username;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

	public String getToken() {
		return token;
	}
	
	 public JwtAuthenticationResponse(String username, Collection<? extends GrantedAuthority> authorities, String token) {
	        this.username = username;
	        this.authorities = authorities;
	        this.token = token;
	    }
    
    
}
