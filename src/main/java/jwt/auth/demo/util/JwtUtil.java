package jwt.auth.demo.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtUtil {

  private static final String SECRET_KEY = "MySecretKey";
  private static final long ACCESS_TOKEN_VALIDITY = 1000 * 60 * 30; // 30 minutes
  private static final long REFRESH_TOKEN_VALIDITY = 1000L * 60 * 60 * 24 * 7; // 7 days

  public String generateAccessToken(String email) {
    Claims claims = Jwts.claims().setSubject(email);
    Date now = new Date();
    Date validity = new Date(now.getTime() + ACCESS_TOKEN_VALIDITY);

    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(validity)
        .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
        .compact();
  }

  public String generateRefreshToken(String email) {
    Claims claims = Jwts.claims().setSubject(email);
    Date now = new Date();
    Date validity = new Date(now.getTime() + REFRESH_TOKEN_VALIDITY);

    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(validity)
        .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
        .compact();
  }

  public String getEmail(String token) {
    return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
  }

  public boolean validateToken(String token) {
    try {
      Jws<Claims> claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
