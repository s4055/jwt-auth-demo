package jwt.auth.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Optional;
import jwt.auth.demo.dto.request.LoginRequest;
import jwt.auth.demo.dto.request.LogoutRequest;
import jwt.auth.demo.dto.request.SignUpRequest;
import jwt.auth.demo.dto.request.WithdrawRequest;
import jwt.auth.demo.dto.response.LoginResponse;
import jwt.auth.demo.dto.response.LogoutResponse;
import jwt.auth.demo.dto.response.SignUpResponse;
import jwt.auth.demo.dto.response.WithdrawResponse;
import jwt.auth.demo.entity.Users;
import jwt.auth.demo.exception.CustomException;
import jwt.auth.demo.exception.ErrorCode;
import jwt.auth.demo.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

  @InjectMocks private UserServiceImpl userService;

  @Mock private UserRepository userRepository;

  @Test
  void 회원가입_이메일_존재() {
    SignUpRequest request =
        new SignUpRequest("user1@example.com", "1111aaaa!!!!", "test", LocalDate.now());
    Users users = new Users(request);
    when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(users));

    CustomException exception =
        assertThrows(CustomException.class, () -> userService.signUp(request));
    assertEquals(ErrorCode.ALREADY_USER.getResultCode(), exception.getErrorCode());
    assertEquals(ErrorCode.ALREADY_USER.getMessage(), exception.getMessage());

    verify(userRepository, never()).save(any(Users.class));
  }

  @Test
  void 회원가입_이메일_미존재() throws CustomException {
    SignUpRequest request =
        new SignUpRequest("user9@example.com", "1111aaaa!!!!", "test", LocalDate.now());
    when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
    when(userRepository.save(any(Users.class))).thenReturn(new Users(request));

    SignUpResponse response = userService.signUp(request);

    assertEquals(ErrorCode.OK.getResultCode(), response.getResultCode());
    assertEquals(ErrorCode.OK.getMessage(), response.getMessage());

    verify(userRepository, times(1)).save(any(Users.class));
  }

  @Test
  void 로그인_회원_정보_미존재() {
    LoginRequest request = new LoginRequest("user1@example.com", "password123!!");

    when(userRepository.findByEmailAndPassword(request.getEmail(), request.getPassword()))
        .thenReturn(Optional.empty());

    CustomException exception =
        assertThrows(CustomException.class, () -> userService.login(request));
    assertEquals(ErrorCode.NOT_FOUND_USER.getResultCode(), exception.getErrorCode());
    assertEquals(ErrorCode.NOT_FOUND_USER.getMessage(), exception.getMessage());

    verify(userRepository, times(1))
        .findByEmailAndPassword(request.getEmail(), request.getPassword());
  }

  @Test
  void 로그인_회원_정보_존재() throws CustomException {
    LoginRequest request = new LoginRequest("user1@example.com", "password123!!");

    when(userRepository.findByEmailAndPassword(request.getEmail(), request.getPassword()))
        .thenReturn(Optional.of(new Users()));

    LoginResponse response = userService.login(request);

    assertEquals(ErrorCode.OK.getResultCode(), response.getResultCode());
    assertEquals(ErrorCode.OK.getMessage(), response.getMessage());

    verify(userRepository, times(1))
        .findByEmailAndPassword(request.getEmail(), request.getPassword());
  }

  @Test
  void 로그아웃_회원_정보_미존재() {
    LogoutRequest request = new LogoutRequest("user1@example.com", "password123!!");

    when(userRepository.findByEmailAndPassword(request.getEmail(), request.getPassword()))
        .thenReturn(Optional.empty());

    CustomException exception =
        assertThrows(CustomException.class, () -> userService.logout(request));
    assertEquals(ErrorCode.NOT_FOUND_USER.getResultCode(), exception.getErrorCode());
    assertEquals(ErrorCode.NOT_FOUND_USER.getMessage(), exception.getMessage());

    verify(userRepository, times(1))
        .findByEmailAndPassword(request.getEmail(), request.getPassword());
  }

  @Test
  void 로그아웃_회원_정보_존재() throws CustomException {
    LogoutRequest request = new LogoutRequest("user1@example.com", "password123!!");

    when(userRepository.findByEmailAndPassword(request.getEmail(), request.getPassword()))
        .thenReturn(Optional.of(new Users()));

    LogoutResponse response = userService.logout(request);

    assertEquals(ErrorCode.OK.getResultCode(), response.getResultCode());
    assertEquals(ErrorCode.OK.getMessage(), response.getMessage());

    verify(userRepository, times(1))
        .findByEmailAndPassword(request.getEmail(), request.getPassword());
  }

  @Test
  void 탈퇴_이메일_존재() throws CustomException {
    WithdrawRequest request = new WithdrawRequest("user1@example.com");
    Users user = mock(Users.class);
    when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));

    WithdrawResponse response = userService.withdraw(request);

    assertEquals(ErrorCode.OK.getResultCode(), response.getResultCode());
    assertEquals(ErrorCode.OK.getMessage(), response.getMessage());

    verify(userRepository, times(1)).findByEmail(request.getEmail());
    verify(user, times(1)).withdraw();
  }

  @Test
  void 탈퇴_이메일_미존재() throws CustomException {
    WithdrawRequest request = new WithdrawRequest("user1@example.com");
    when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

    CustomException exception =
        assertThrows(CustomException.class, () -> userService.withdraw(request));
    assertEquals(ErrorCode.NOT_FOUND_USER.getResultCode(), exception.getErrorCode());
    assertEquals(ErrorCode.NOT_FOUND_USER.getMessage(), exception.getMessage());

    verify(userRepository, times(1)).findByEmail(request.getEmail());
  }
}
