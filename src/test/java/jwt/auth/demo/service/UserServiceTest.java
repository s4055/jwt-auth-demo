package jwt.auth.demo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import jwt.auth.demo.dto.request.LoginRequest;
import jwt.auth.demo.dto.request.SignupRequest;
import jwt.auth.demo.dto.response.SignupResponse;
import jwt.auth.demo.dto.response.TokenResponse;
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
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

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

    when(jwtUtil.generateAccessToken(request.getEmail()))
            .thenReturn("access-token");
    when(jwtUtil.generateRefreshToken(request.getEmail()))
            .thenReturn("refresh-token");

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
  //
  //  @Test
  //  void 로그아웃_회원_정보_미존재() {
  //    LogoutRequest request = new LogoutRequest("user1@example.com", "password123!!");
  //
  //    when(userRepository.findByEmailAndPassword(request.getEmail(), request.getPassword()))
  //        .thenReturn(Optional.empty());
  //
  //    CustomException exception =
  //        assertThrows(CustomException.class, () -> userService.logout(request));
  //    assertEquals(ErrorCode.NOT_FOUND_USER.getResultCode(), exception.getErrorCode());
  //    assertEquals(ErrorCode.NOT_FOUND_USER.getMessage(), exception.getMessage());
  //
  //    verify(userRepository, times(1))
  //        .findByEmailAndPassword(request.getEmail(), request.getPassword());
  //  }
  //
  //  @Test
  //  void 로그아웃_회원_정보_존재() throws CustomException {
  //    LogoutRequest request = new LogoutRequest("user1@example.com", "password123!!");
  //
  //    when(userRepository.findByEmailAndPassword(request.getEmail(), request.getPassword()))
  //        .thenReturn(Optional.of(new Users()));
  //
  //    LogoutResponse response = userService.logout(request);
  //
  //    assertEquals(ErrorCode.OK.getResultCode(), response.getResultCode());
  //    assertEquals(ErrorCode.OK.getMessage(), response.getMessage());
  //
  //    verify(userRepository, times(1))
  //        .findByEmailAndPassword(request.getEmail(), request.getPassword());
  //  }
  //
  //  @Test
  //  void 탈퇴_이메일_존재() throws CustomException {
  //    WithdrawRequest request = new WithdrawRequest("user1@example.com");
  //    Users user = mock(Users.class);
  //    when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
  //
  //    WithdrawResponse response = userService.withdraw(request);
  //
  //    assertEquals(ErrorCode.OK.getResultCode(), response.getResultCode());
  //    assertEquals(ErrorCode.OK.getMessage(), response.getMessage());
  //
  //    verify(userRepository, times(1)).findByEmail(request.getEmail());
  //    verify(user, times(1)).withdraw();
  //  }
  //
  //  @Test
  //  void 탈퇴_이메일_미존재() throws CustomException {
  //    WithdrawRequest request = new WithdrawRequest("user1@example.com");
  //    when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
  //
  //    CustomException exception =
  //        assertThrows(CustomException.class, () -> userService.withdraw(request));
  //    assertEquals(ErrorCode.NOT_FOUND_USER.getResultCode(), exception.getErrorCode());
  //    assertEquals(ErrorCode.NOT_FOUND_USER.getMessage(), exception.getMessage());
  //
  //    verify(userRepository, times(1)).findByEmail(request.getEmail());
  //  }
}
