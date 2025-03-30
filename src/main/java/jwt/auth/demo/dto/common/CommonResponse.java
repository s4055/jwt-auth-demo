package jwt.auth.demo.dto.common;

import jwt.auth.demo.exception.ErrorCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonResponse {
  private String resultCode;
  private String message;

  public CommonResponse(ErrorCode errorCode) {
    this.resultCode = errorCode.getResultCode();
    this.message = errorCode.getMessage();
  }

  public CommonResponse(String errorCode, String errorMessage) {
    this.resultCode = errorCode;
    this.message = errorMessage;
  }
}
