package org.realworld.demo.jwt;

import java.util.Collection;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

  private final Object principal;

  private final String credentials;


  public JwtAuthenticationToken(Object principal, String credentials) {
    super(null);
    setAuthenticated(false);
    this.principal = principal;
    this.credentials = credentials;
  }

  public JwtAuthenticationToken(Object principal, String credentials,
      Collection<? extends GrantedAuthority> authorities) {
    super(authorities);
    setAuthenticated(true);
    this.principal = principal;
    this.credentials = credentials;
  }


  @Override
  public String getCredentials() {
    return credentials;
  }

  @Override
  public Object getPrincipal() {
    return principal;
  }
}
