package jwt.auth.demo.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtUtil {

  private static final String SECRET_KEY = "MySecretKeyForJWT";
  private static final long VALIDITY_IN_MILLISECONDS = 3600000;

  // JWT 생성
  public static String createToken(String email) {
    Claims claims = Jwts.claims().setSubject(email);
    Date now = new Date();
    Date validity = new Date(now.getTime() + VALIDITY_IN_MILLISECONDS);

    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(validity)
        .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
        .compact();
  }

  // 토큰에서 이메일 추출
  public static String getEmail(String token) {
    return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
  }

  // 토큰 검증
  public static boolean validateToken(String token) {
    try {
      Jws<Claims> claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
