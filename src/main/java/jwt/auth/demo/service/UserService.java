package jwt.auth.demo.service;

import jwt.auth.demo.dto.request.LoginRequest;
import jwt.auth.demo.dto.request.LogoutRequest;
import jwt.auth.demo.dto.request.SignUpRequest;
import jwt.auth.demo.dto.request.WithdrawRequest;
import jwt.auth.demo.dto.response.LoginResponse;
import jwt.auth.demo.dto.response.LogoutResponse;
import jwt.auth.demo.dto.response.SignUpResponse;
import jwt.auth.demo.dto.response.WithdrawResponse;
import jwt.auth.demo.exception.CustomException;
import org.springframework.transaction.annotation.Transactional;

public interface UserService {

  @Transactional(readOnly = false)
  SignUpResponse signUp(SignUpRequest request) throws CustomException;

  @Transactional(readOnly = true)
  LoginResponse login(LoginRequest request) throws CustomException;

  @Transactional(readOnly = true)
  LogoutResponse logout(LogoutRequest request) throws CustomException;

  @Transactional(readOnly = false)
  WithdrawResponse withdraw(WithdrawRequest request) throws CustomException;
}
