package org.realworld.demo.jwt;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "jwt")
@Configuration
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JwtConfiguration {

  private String clientSecret;

  private String header;

  private String issuer;

  private int expirySeconds;
}
