package jwt.auth.demo.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtUtil {

  private static final String SECRET_KEY = "MySecretKey";
  private static final long ACCESS_TOKEN_VALIDITY = 1000 * 60 * 30; // 30분
  private static final long REFRESH_TOKEN_VALIDITY = 1000L * 60 * 60 * 24 * 7; // 7일

  public static String generateAccessToken(String email) {
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

  public static String generateRefreshToken(String email) {
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

  public static String getEmail(String token) {
    return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
  }

  public static boolean validateToken(String token) {
    try {
      Jws<Claims> claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
