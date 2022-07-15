package org.realworld.demo.jwt;

import org.realworld.demo.domain.user.entity.User;
import org.realworld.demo.domain.user.service.UserService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;

public class JwtAuthenticationProvider implements AuthenticationProvider {

  private final UserService userService;

  private final JwtUtil jwtUtil;

  public JwtAuthenticationProvider(UserService userService, JwtUtil jwtUtil) {
    this.userService = userService;
    this.jwtUtil = jwtUtil;
  }

  @Override
  public Authentication authenticate(Authentication authentication)
      throws IllegalArgumentException {
    JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
    return processUserAuthentication(token);
  }

  private Authentication processUserAuthentication(JwtAuthenticationToken token) {
    String email = (String) token.getPrincipal();
    String password = token.getCredentials();

    User loginUser = userService.login(email, password);
    String[] roles = loginUser.getAuthorities()
        .stream().map(Object::toString)
        .toList().toArray(String[]::new);

    String jwt = jwtUtil.createToken(JwtUtil.Claims.from(loginUser.getId(), email, roles));

    JwtPrincipal jwtPrincipal = new JwtPrincipal(jwt, loginUser.getId());

    return new JwtAuthenticationToken(jwtPrincipal, null, loginUser.getAuthorities());
  }


  @Override
  public boolean supports(Class<?> authentication) {
    return JwtAuthenticationToken.class.isAssignableFrom(authentication);
  }
}
