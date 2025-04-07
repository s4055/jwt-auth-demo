package jwt.auth.demo.service;

import jakarta.servlet.http.HttpServletRequest;
import jwt.auth.demo.dto.request.LoginRequest;
import jwt.auth.demo.dto.request.LogoutRequest;
import jwt.auth.demo.dto.request.SignupRequest;
import jwt.auth.demo.dto.request.WithdrawRequest;
import jwt.auth.demo.dto.response.*;
import jwt.auth.demo.exception.CustomException;
import org.springframework.transaction.annotation.Transactional;

public interface UserService {

  @Transactional(readOnly = false)
  SignupResponse signup(SignupRequest request) throws CustomException;

  @Transactional(readOnly = true)
  TokenResponse login(LoginRequest request) throws CustomException;

  @Transactional(readOnly = true)
  LogoutResponse logout(LogoutRequest request, HttpServletRequest httpServletRequest)
      throws CustomException;

  @Transactional(readOnly = false)
  WithdrawResponse withdraw(WithdrawRequest request, HttpServletRequest httpServletRequest)
      throws CustomException;

  RefreshTokenResponse reissue(HttpServletRequest httpServletRequest) throws CustomException;
}
