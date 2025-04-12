package jwt.auth.demo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import jwt.auth.demo.dto.request.LoginRequest;
import jwt.auth.demo.dto.request.LogoutRequest;
import jwt.auth.demo.dto.request.SignupRequest;
import jwt.auth.demo.dto.request.WithdrawRequest;
import jwt.auth.demo.dto.response.*;
import jwt.auth.demo.entity.Users;
import jwt.auth.demo.enums.UserStatus;
import jwt.auth.demo.exception.CustomException;
import jwt.auth.demo.exception.ErrorCode;
import jwt.auth.demo.repository.UserRepository;
import jwt.auth.demo.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

  @InjectMocks private UserServiceImpl userService;
  @Mock private UserRepository userRepository;
  @Mock private JwtUtil jwtUtil;
  @Mock private RedisTemplate<String, String> redisTemplate;
  @Mock private ValueOperations<String, String> valueOperations;

  @Test
  void 회원가입_중복_이메일() {
    SignupRequest request =
        new SignupRequest("user1@example.com", "1111aaaa!!!!", "test", LocalDate.now());

    Users users =
        new Users(
            UUID.randomUUID().toString(),
            request.getEmail(),
            "bbbb3333!!!!",
            "test2",
            LocalDate.of(2000, 11, 2),
            UserStatus.ACTIVE,
            LocalDateTime.now());

    when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(users));

    CustomException exception =
        assertThrows(CustomException.class, () -> userService.signup(request));
    assertEquals(ErrorCode.ALREADY_USER.getResultCode(), exception.getErrorCode());
    assertEquals(ErrorCode.ALREADY_USER.getMessage(), exception.getMessage());

    verify(userRepository, never()).save(any(Users.class));
  }

  @Test
  void 회원가입_성공() throws CustomException {
    SignupRequest request =
        new SignupRequest("user9@example.com", "1111aaaa!!!!", "test", LocalDate.now());

    Users users =
        new Users(
            UUID.randomUUID().toString(),
            request.getEmail(),
            request.getPassword(),
            request.getName(),
            request.getBirthDt(),
            UserStatus.ACTIVE,
            LocalDateTime.now());

    when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
    when(userRepository.save(any(Users.class))).thenReturn(users);

    SignupResponse response = userService.signup(request);

    assertEquals(ErrorCode.OK.getResultCode(), response.getResultCode());
    assertEquals(ErrorCode.OK.getMessage(), response.getMessage());

    verify(userRepository, times(1)).save(any(Users.class));
  }

  @Test
  void 로그인_없는_회원() {
    LoginRequest request = new LoginRequest("user1@example.com", "password123!!");

    when(userRepository.findByEmailAndPasswordAndStatus(
            request.getEmail(), request.getPassword(), UserStatus.ACTIVE))
        .thenReturn(Optional.empty());

    CustomException exception =
        assertThrows(CustomException.class, () -> userService.login(request));
    assertEquals(ErrorCode.NOT_FOUND_USER.getResultCode(), exception.getErrorCode());
    assertEquals(ErrorCode.NOT_FOUND_USER.getMessage(), exception.getMessage());

    verify(userRepository, times(1))
        .findByEmailAndPasswordAndStatus(
            request.getEmail(), request.getPassword(), UserStatus.ACTIVE);
  }

  @Test
  void 로그인_성공() throws CustomException {
    LoginRequest request = new LoginRequest("user1@example.com", "password123!!");

    Users users =
        new Users(
            UUID.randomUUID().toString(),
            request.getEmail(),
            request.getPassword(),
            "test",
            LocalDate.now(),
            UserStatus.ACTIVE,
            LocalDateTime.now());

    when(userRepository.findByEmailAndPasswordAndStatus(
            request.getEmail(), request.getPassword(), UserStatus.ACTIVE))
        .thenReturn(Optional.of(users));

    when(jwtUtil.generateAccessToken(request.getEmail())).thenReturn("access-token");
    when(jwtUtil.generateRefreshToken(request.getEmail())).thenReturn("refresh-token");

    when(redisTemplate.opsForValue()).thenReturn(valueOperations);

    TokenResponse response = userService.login(request);

    assertNotNull(response);
    assertEquals("access-token", response.getAccessToken());
    assertEquals("refresh-token", response.getRefreshToken());

    verify(valueOperations, times(1))
        .set(eq(request.getEmail()), eq("refresh-token"), eq(7L), eq(TimeUnit.DAYS));

    verify(userRepository, times(1))
        .findByEmailAndPasswordAndStatus(
            request.getEmail(), request.getPassword(), UserStatus.ACTIVE);
  }

  @Test
  void 로그아웃_없는_회원() {
    LogoutRequest request = new LogoutRequest("password123!!");

    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn("test@example.com");

    try (MockedStatic<SecurityContextHolder> mockedStatic =
        mockStatic(SecurityContextHolder.class)) {
      SecurityContext securityContext = mock(SecurityContext.class);
      when(securityContext.getAuthentication()).thenReturn(authentication);
      mockedStatic.when(SecurityContextHolder::getContext).thenReturn(securityContext);

      when(userRepository.findByEmailAndPasswordAndStatus(
              authentication.getName(), request.getPassword(), UserStatus.ACTIVE))
          .thenReturn(Optional.empty());

      CustomException exception =
          assertThrows(
              CustomException.class,
              () -> userService.logout(request, mock(HttpServletRequest.class)));
      assertEquals(ErrorCode.NOT_FOUND_USER.getResultCode(), exception.getErrorCode());
      assertEquals(ErrorCode.NOT_FOUND_USER.getMessage(), exception.getMessage());

      verify(userRepository, times(1))
          .findByEmailAndPasswordAndStatus(
              "test@example.com", request.getPassword(), UserStatus.ACTIVE);
    }
  }

  @Test
  void 로그아웃_성공() throws CustomException {
    LogoutRequest request = new LogoutRequest("password123!!");

    Users users =
        new Users(
            UUID.randomUUID().toString(),
            "user1@example.com",
            request.getPassword(),
            "test_name",
            LocalDate.now(),
            UserStatus.ACTIVE,
            LocalDateTime.now());

    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn("user1@example.com");

    try (MockedStatic<SecurityContextHolder> mockedStatic =
        mockStatic(SecurityContextHolder.class)) {
      SecurityContext securityContext = mock(SecurityContext.class);
      when(securityContext.getAuthentication()).thenReturn(authentication);
      mockedStatic.when(SecurityContextHolder::getContext).thenReturn(securityContext);

      when(userRepository.findByEmailAndPasswordAndStatus(
              authentication.getName(), request.getPassword(), UserStatus.ACTIVE))
          .thenReturn(Optional.of(users));

      LogoutResponse response = userService.logout(request, mock(HttpServletRequest.class));

      assertEquals(ErrorCode.OK.getResultCode(), response.getResultCode());
      assertEquals(ErrorCode.OK.getMessage(), response.getMessage());

      verify(userRepository, times(1))
          .findByEmailAndPasswordAndStatus(
              authentication.getName(), request.getPassword(), UserStatus.ACTIVE);
    }
  }

  @Test
  void 탈퇴_없는_회원() throws CustomException {
    WithdrawRequest request = new WithdrawRequest("password123!!");

    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn("test@example.com");

    try (MockedStatic<SecurityContextHolder> mockedStatic =
        mockStatic(SecurityContextHolder.class)) {
      SecurityContext securityContext = mock(SecurityContext.class);
      when(securityContext.getAuthentication()).thenReturn(authentication);
      mockedStatic.when(SecurityContextHolder::getContext).thenReturn(securityContext);

      when(userRepository.findByEmailAndPasswordAndStatus(
              authentication.getName(), request.getPassword(), UserStatus.ACTIVE))
          .thenReturn(Optional.empty());

      CustomException exception =
          assertThrows(
              CustomException.class,
              () -> userService.withdraw(request, mock(HttpServletRequest.class)));
      assertEquals(ErrorCode.NOT_FOUND_USER.getResultCode(), exception.getErrorCode());
      assertEquals(ErrorCode.NOT_FOUND_USER.getMessage(), exception.getMessage());

      verify(userRepository, times(1))
          .findByEmailAndPasswordAndStatus(
              "test@example.com", request.getPassword(), UserStatus.ACTIVE);
    }
  }

  @Test
  void 탈퇴_성공() throws CustomException {
    WithdrawRequest request = new WithdrawRequest("password123!!");

    Users users =
        new Users(
            UUID.randomUUID().toString(),
            "user1@example.com",
            request.getPassword(),
            "test_name",
            LocalDate.now(),
            UserStatus.ACTIVE,
            LocalDateTime.now());

    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn("user1@example.com");

    try (MockedStatic<SecurityContextHolder> mockedStatic =
        mockStatic(SecurityContextHolder.class)) {
      SecurityContext securityContext = mock(SecurityContext.class);
      when(securityContext.getAuthentication()).thenReturn(authentication);
      mockedStatic.when(SecurityContextHolder::getContext).thenReturn(securityContext);

      when(userRepository.findByEmailAndPasswordAndStatus(
              authentication.getName(), request.getPassword(), UserStatus.ACTIVE))
          .thenReturn(Optional.of(users));

      WithdrawResponse response = userService.withdraw(request, mock(HttpServletRequest.class));

      assertEquals(ErrorCode.OK.getResultCode(), response.getResultCode());
      assertEquals(ErrorCode.OK.getMessage(), response.getMessage());

      verify(userRepository, times(1))
          .findByEmailAndPasswordAndStatus(
              authentication.getName(), request.getPassword(), UserStatus.ACTIVE);
      verify(redisTemplate, times(1)).delete("user1@example.com");
    }
  }

  @Test
  void 재발급_리프레시_토큰_없음() {
    MockHttpServletRequest request = new MockHttpServletRequest();
    CustomException exception =
        assertThrows(
            CustomException.class,
            () -> {
              userService.reissue(request);
            });

    assertEquals(ErrorCode.NO_REFRESH_TOKEN.getResultCode(), exception.getErrorCode());
    assertEquals(ErrorCode.NO_REFRESH_TOKEN.getMessage(), exception.getMessage());
  }

  @Test
  void 재발급_리프레시_토큰_유효하지_않음() {
    String refreshToken = "invalid_refresh_token";

    when(jwtUtil.validateToken(refreshToken)).thenReturn(false);

    MockHttpServletRequest request = new MockHttpServletRequest();
    Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
    request.setCookies(refreshCookie);

    CustomException exception =
        assertThrows(
            CustomException.class,
            () -> {
              userService.reissue(request);
            });

    assertEquals(ErrorCode.UNAUTHORIZED_TOKEN.getResultCode(), exception.getErrorCode());
    assertEquals(ErrorCode.UNAUTHORIZED_TOKEN.getMessage(), exception.getMessage());
  }

  @Test
  void 재발급_리프레시_토큰_불일치() {
    String refreshToken = "valid_refresh_token";
    String email = "test@example.com";
    String storedToken = "different_refresh_token";

    when(jwtUtil.validateToken(refreshToken)).thenReturn(true);
    when(jwtUtil.getEmail(refreshToken)).thenReturn(email);
    when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    when(valueOperations.get(email)).thenReturn(storedToken);

    MockHttpServletRequest request = new MockHttpServletRequest();
    Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
    request.setCookies(refreshCookie);

    CustomException exception =
        assertThrows(
            CustomException.class,
            () -> {
              userService.reissue(request);
            });

    assertEquals(ErrorCode.REFRESH_TOKEN_MISMATCH.getResultCode(), exception.getErrorCode());
    assertEquals(ErrorCode.REFRESH_TOKEN_MISMATCH.getMessage(), exception.getMessage());
  }

  @Test
  void 재발급_성공() throws CustomException {
    String refreshToken = "valid_refresh_token";
    String email = "test@example.com";
    String storedToken = "valid_refresh_token";
    String newAccessToken = "new_access_token";

    when(jwtUtil.validateToken(refreshToken)).thenReturn(true);
    when(jwtUtil.getEmail(refreshToken)).thenReturn(email);
    when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    when(valueOperations.get(email)).thenReturn(storedToken);
    when(jwtUtil.generateAccessToken(email)).thenReturn(newAccessToken);

    MockHttpServletRequest request = new MockHttpServletRequest();
    Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
    request.setCookies(refreshCookie);

    RefreshTokenResponse response = userService.reissue(request);

    assertEquals(ErrorCode.OK.getResultCode(), response.getResultCode());
    assertEquals(ErrorCode.OK.getMessage(), response.getMessage());
    assertEquals(newAccessToken, response.getNewAccessToken());

    verify(jwtUtil, times(1)).validateToken(refreshToken);
    verify(jwtUtil, times(1)).getEmail(refreshToken);
    verify(valueOperations, times(1)).get(email);
    verify(jwtUtil, times(1)).generateAccessToken(email);
  }
}
