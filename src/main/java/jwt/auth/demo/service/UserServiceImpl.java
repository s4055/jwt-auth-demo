package jwt.auth.demo.service;

import java.util.Optional;
import jwt.auth.demo.dto.request.LoginRequest;
import jwt.auth.demo.dto.request.LogoutRequest;
import jwt.auth.demo.dto.request.SignupRequest;
import jwt.auth.demo.dto.request.WithdrawRequest;
import jwt.auth.demo.dto.response.LoginResponse;
import jwt.auth.demo.dto.response.LogoutResponse;
import jwt.auth.demo.dto.response.SignupResponse;
import jwt.auth.demo.dto.response.WithdrawResponse;
import jwt.auth.demo.entity.Users;
import jwt.auth.demo.enums.UserStatus;
import jwt.auth.demo.exception.CustomException;
import jwt.auth.demo.exception.ErrorCode;
import jwt.auth.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  @Override
  public SignupResponse signup(SignupRequest request) throws CustomException {
    Optional<Users> user = userRepository.findByEmail(request.getEmail());

    if (user.isPresent()) {
      throw new CustomException(ErrorCode.ALREADY_USER);
    }

    Users users = userRepository.save(new Users(request));

    log.info("유저 저장 = {}", users.getId());

    return new SignupResponse(ErrorCode.OK);
  }

  @Override
  public LoginResponse login(LoginRequest request) throws CustomException {
    Users users =
        userRepository
            .findByEmailAndPasswordAndStatus(
                request.getEmail(), request.getPassword(), UserStatus.ACTIVE)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

    log.info("로그인 유저 = {}", users.getName());

    return new LoginResponse(ErrorCode.OK);
  }

  @Override
  public LogoutResponse logout(LogoutRequest request) throws CustomException {
    Users users =
        userRepository
            .findByEmailAndPasswordAndStatus(
                request.getEmail(), request.getPassword(), UserStatus.ACTIVE)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

    log.info("로그아웃 유저 = {}", users.getName());

    return new LogoutResponse(ErrorCode.OK);
  }

  @Override
  public WithdrawResponse withdraw(WithdrawRequest request) throws CustomException {
    Users user =
        userRepository
            .findByEmailAndPasswordAndStatus(
                request.getEmail(), request.getPassword(), UserStatus.ACTIVE)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

    user.withdraw();
    return new WithdrawResponse(ErrorCode.OK);
  }
}
