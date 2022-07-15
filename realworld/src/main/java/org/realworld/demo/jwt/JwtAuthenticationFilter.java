package org.realworld.demo.jwt;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.realworld.demo.controller.dto.UserDto.UserLoginRequest;
import org.realworld.demo.jwt.JwtUtil.Claims;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Slf4j
public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

  private final ObjectMapper objectMapper = new ObjectMapper();

  private final JwtUtil jwtUtil;


  public JwtAuthenticationFilter(JwtUtil jwtUtil, AuthenticationProvider authenticationProvider) {
    super(new AntPathRequestMatcher("/**/login", "POST"),
        new ProviderManager(authenticationProvider));
    this.jwtUtil = jwtUtil;
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    this.doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
  }

  public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws ServletException, IOException {
    if (requiresAuthentication(request, response)) {
      try {
        Authentication token = this.attemptAuthentication(request, response);
        SecurityContextHolder.getContext().setAuthentication(token);
      } catch (IllegalArgumentException ex) {
        log.trace("아이디, 비밀번호가 일치하지 않습니다");
        chain.doFilter(request, response);
      }
    } else if (getToken(request) != null) {
      String token = getToken(request);
      JwtAuthenticationToken authenticationToken = this.createAuthentication(token);

      if (authenticationToken != null) {
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
      }
    }
    chain.doFilter(request, response);
  }

  private JwtAuthenticationToken createAuthentication(String token) {
    try {
      Claims claims = jwtUtil.verifyToken(token);
      String jwt = jwtUtil.createToken(claims);

      return new JwtAuthenticationToken(
          new JwtPrincipal(jwt, claims.getUserId()),
          null,
          Arrays.stream(claims.getRoles()).map(SimpleGrantedAuthority::new).toList());

    } catch (JWTVerificationException ex) {
      return null;
    }
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse)
      throws AuthenticationException, IOException {

    String body = getBody(httpServletRequest);
    UserLoginRequest loginRequest = objectMapper.readValue(body, UserLoginRequest.class);
    JwtAuthenticationToken token = new JwtAuthenticationToken(loginRequest.getEmail(),
        loginRequest.getPassword());
    return this.getAuthenticationManager().authenticate(token);
  }

  private String getBody(HttpServletRequest request) throws IOException {

    String body;
    StringBuilder stringBuilder = new StringBuilder();
    BufferedReader bufferedReader = null;

    try {
      InputStream inputStream = request.getInputStream();
      if (inputStream != null) {
        bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        char[] charBuffer = new char[128];
        int bytesRead = -1;
        while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
          stringBuilder.append(charBuffer, 0, bytesRead);
        }
      }
    } catch (IOException ex) {
      throw ex;
    } finally {
      if (bufferedReader != null) {
        try {
          bufferedReader.close();
        } catch (IOException ex) {
          throw ex;
        }
      }
    }

    body = stringBuilder.toString();
    return body;
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
