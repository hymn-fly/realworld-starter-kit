package org.realworld.demo.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.impl.NullClaim;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
public final class JwtUtil {

  private final JwtConfiguration configuration;

  private final Algorithm algorithm;

  private final JWTVerifier verifier;

  public JwtUtil(JwtConfiguration jwtConfiguration) {
    this.configuration = jwtConfiguration;
    this.algorithm = Algorithm.HMAC512(jwtConfiguration.getClientSecret());
    this.verifier = JWT.require(this.algorithm)
        .withIssuer(configuration.getIssuer())
        .build();
  }

  public String createToken(Claims claims) {
    Date now = new Date(Calendar.getInstance().getTimeInMillis());
    Date expireDate = new Date(now.getTime() + configuration.getExpirySeconds() * 1000L);

    return JWT.create()
        .withIssuer(configuration.getIssuer())
        .withExpiresAt(expireDate)
        .withIssuedAt(now)

        .withClaim("email", claims.email)
        .withArrayClaim("roles", claims.roles)
        .sign(algorithm);
  }

  public Claims verifyToken(String token) {
    DecodedJWT jwt = verifier.verify(token);
    return new Claims(jwt);
  }

  @Getter
  public static class Claims {

    private final Long userId;

    private final String email;

    private final String[] roles;

    private String iss;

    private Date exp;

    private Date iat;

    // Jwt token verify시 사용하는 생성자
    private Claims(DecodedJWT decodedJWT) {
      Map<String, Claim> claims = decodedJWT.getClaims();
      this.email = claims.getOrDefault("email", new NullClaim()).asString();
      this.roles = claims.getOrDefault("roles", new NullClaim()).asArray(String.class);
      this.userId = claims.getOrDefault("userId", new NullClaim()).asLong();

      this.exp = decodedJWT.getExpiresAt();
      this.iat = decodedJWT.getIssuedAt();
      this.iss = decodedJWT.getIssuer();
    }

    private Claims(Long userId, String email, String... roles) {
      this.email = email;
      this.roles = roles;
      this.userId = userId;
    }

    // Jwt Token만들 때, 사용하는 정적 메소드
    public static Claims from(Long userId, String email, String... roles) {
      return new Claims(userId, email, roles);
    }

  }

}
