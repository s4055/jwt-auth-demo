package jwt.auth.demo.service;

import jwt.auth.demo.dto.request.LoginRequest;
import jwt.auth.demo.dto.request.LogoutRequest;
import jwt.auth.demo.dto.request.SignupRequest;
import jwt.auth.demo.dto.request.WithdrawRequest;
import jwt.auth.demo.dto.response.LoginResponse;
import jwt.auth.demo.dto.response.LogoutResponse;
import jwt.auth.demo.dto.response.SignupResponse;
import jwt.auth.demo.dto.response.WithdrawResponse;
import jwt.auth.demo.exception.CustomException;
import org.springframework.transaction.annotation.Transactional;

public interface UserService {

  @Transactional(readOnly = false)
  SignupResponse signup(SignupRequest request) throws CustomException;

  @Transactional(readOnly = true)
  LoginResponse login(LoginRequest request) throws CustomException;

  @Transactional(readOnly = true)
  LogoutResponse logout(LogoutRequest request, String token) throws CustomException;

  @Transactional(readOnly = false)
  WithdrawResponse withdraw(WithdrawRequest request, String token) throws CustomException;
}
