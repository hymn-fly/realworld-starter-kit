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
public final class Jwt {

  private final JwtConfiguration configuration;

  private final Algorithm algorithm;

  private final JWTVerifier verifier;

  public Jwt(JwtConfiguration jwtConfiguration) {
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
        .withClaim("id", claims.getId())
        .withClaim("email", claims.email)
        .sign(algorithm);
  }

  public Claims verifyToken(String token) {
    DecodedJWT jwt = verifier.verify(token);
    return new Claims(jwt);
  }

  @Getter
  public static class Claims {

    private final Long id;

    private final String email;

    private String iss;

    private Date exp;

    private Date iat;

    // Jwt token verify시 사용하는 생성자
    private Claims(DecodedJWT decodedJWT) {
      Map<String, Claim> claims = decodedJWT.getClaims();
      this.email = claims.getOrDefault("email", new NullClaim()).asString();
      this.id = claims.getOrDefault("id", new NullClaim()).asLong();

      this.exp = decodedJWT.getExpiresAt();
      this.iat = decodedJWT.getIssuedAt();
      this.iss = decodedJWT.getIssuer();
    }

    private Claims(Long id, String email) {
      this.email = email;
      this.id = id;
    }

    // Jwt Token만들 때, 사용하는 정적 메소드
    public static Claims from(Long userId, String email) {
      return new Claims(userId, email);
    }

  }

}
