package jwt.auth.demo.controller;

import jakarta.validation.Valid;
import jwt.auth.demo.dto.request.LoginRequest;
import jwt.auth.demo.dto.request.LogoutRequest;
import jwt.auth.demo.dto.request.SignUpRequest;
import jwt.auth.demo.dto.request.WithdrawRequest;
import jwt.auth.demo.dto.response.LoginResponse;
import jwt.auth.demo.dto.response.LogoutResponse;
import jwt.auth.demo.dto.response.SignUpResponse;
import jwt.auth.demo.dto.response.WithdrawResponse;
import jwt.auth.demo.exception.CustomException;
import jwt.auth.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class UserController {

  private final UserService userService;

  @PostMapping("/signup")
  public ResponseEntity<SignUpResponse> signUp(@RequestBody @Valid SignUpRequest request)
      throws CustomException {
    SignUpResponse response = userService.signUp(request);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request)
      throws CustomException {
    LoginResponse response = userService.login(request);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @PostMapping("/logout")
  public ResponseEntity<LogoutResponse> logout(@RequestBody @Valid LogoutRequest request)
      throws CustomException {
    LogoutResponse response = userService.logout(request);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @DeleteMapping("/delete")
  public ResponseEntity<WithdrawResponse> withdraw(@RequestBody @Valid WithdrawRequest request)
      throws CustomException {
    WithdrawResponse response = userService.withdraw(request);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }
}
