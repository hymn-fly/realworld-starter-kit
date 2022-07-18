package org.realworld.demo.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class JwtPrincipal {

  private final String token;

  private final Long userId;
}
