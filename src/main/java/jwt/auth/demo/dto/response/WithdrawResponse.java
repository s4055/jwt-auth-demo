package jwt.auth.demo.dto.response;

import jwt.auth.demo.dto.common.CommonResponse;
import jwt.auth.demo.exception.ErrorCode;

public class WithdrawResponse extends CommonResponse {
  public WithdrawResponse(ErrorCode errorCode) {
    super(errorCode);
  }
}
