package jwt.auth.demo.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
  OK("000000", "성공", HttpStatus.OK),
  BAD_REQUEST("000001", "잘못된 요청 데이터", HttpStatus.BAD_REQUEST),
  ALREADY_USER("000002", "이미 있는 사용자", HttpStatus.CONFLICT),
  NOT_FOUND_USER("000003", "없는 사용자", HttpStatus.NOT_FOUND),
  REFRESH_TOKEN_MISMATCH("999996", "리프레시 토큰 불일치", HttpStatus.CONFLICT),
  NO_REFRESH_TOKEN("999997", "없는 리프레시 토큰", HttpStatus.NOT_FOUND),
  UNAUTHORIZED_TOKEN("999998", "유효하지 않은 토큰", HttpStatus.UNAUTHORIZED),
  INTERNAL_SERVER_ERROR("999999", "서버 에러", HttpStatus.INTERNAL_SERVER_ERROR);

  private final String resultCode;
  private final String message;
  private final HttpStatus httpStatus;
}
