package jwt.auth.demo.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jwt.auth.demo.dto.request.LoginRequest;
import jwt.auth.demo.dto.request.LogoutRequest;
import jwt.auth.demo.dto.request.SignupRequest;
import jwt.auth.demo.dto.request.WithdrawRequest;
import jwt.auth.demo.dto.response.*;
import jwt.auth.demo.exception.CustomException;
import jwt.auth.demo.exception.ErrorCode;
import jwt.auth.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class UserController {

  private final UserService userService;

  @PostMapping("/signup")
  public ResponseEntity<SignupResponse> signup(@RequestBody @Valid SignupRequest request)
      throws CustomException {
    SignupResponse response = userService.signup(request);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request)
      throws CustomException {
    TokenResponse tokenResponse = userService.login(request);

    ResponseCookie cookie =
        ResponseCookie.from("refreshToken", tokenResponse.getRefreshToken())
            .httpOnly(true)
            .secure(true)
            .path("/auth/refresh")
            .maxAge(7 * 24 * 60 * 60) // 7 days
            .sameSite("Strict")
            .build();

    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.SET_COOKIE, cookie.toString());

    LoginResponse response = new LoginResponse(ErrorCode.OK, tokenResponse);

    return ResponseEntity.status(HttpStatus.OK).headers(headers).body(response);
  }

  @PostMapping("/logout")
  public ResponseEntity<LogoutResponse> logout(
      @RequestBody @Valid LogoutRequest request, HttpServletRequest httpServletRequest)
      throws CustomException {
    LogoutResponse response = userService.logout(request, httpServletRequest);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @DeleteMapping("/withdraw")
  public ResponseEntity<WithdrawResponse> withdraw(
      @RequestBody @Valid WithdrawRequest request, HttpServletRequest httpServletRequest)
      throws CustomException {
    WithdrawResponse response = userService.withdraw(request, httpServletRequest);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @PostMapping("/refresh")
  public ResponseEntity<RefreshTokenResponse> reissue(HttpServletRequest httpServletRequest)
      throws CustomException {
    RefreshTokenResponse response = userService.reissue(httpServletRequest);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }
}
