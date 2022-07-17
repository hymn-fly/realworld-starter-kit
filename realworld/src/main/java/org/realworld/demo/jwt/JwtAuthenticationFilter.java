package org.realworld.demo.jwt;

import com.auth0.jwt.exceptions.JWTVerificationException;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.realworld.demo.jwt.Jwt.Claims;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;


@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final Jwt jwt;


  public JwtAuthenticationFilter(Jwt jwt) {
    this.jwt = jwt;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse, FilterChain filterChain)
      throws ServletException, IOException {
    if (getToken(httpServletRequest) != null
        && SecurityContextHolder.getContext().getAuthentication() == null) {
      String token = getToken(httpServletRequest);
      JwtAuthenticationToken authenticationToken = this.createAuthentication(token);

      if (authenticationToken != null) {
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
      }
    }
    filterChain.doFilter(httpServletRequest, httpServletResponse);

  }

  private JwtAuthenticationToken createAuthentication(String token) {
    try {
      Claims claims = jwt.verifyToken(token);

      return new JwtAuthenticationToken(
          new JwtPrincipal(token, claims.getUserId()),
          null,
          new ArrayList<>());

    } catch (JWTVerificationException ex) {
      return null;
    }
  }

  private String getToken(ServletRequest request) {
    HttpServletRequest httpServletRequest = (HttpServletRequest) request;
    String token = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
    if (token == null) {
      return null;
    }
    return token.substring("Bearer ".length());
  }
}
