package jwt.auth.demo.service;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final RedisTemplate<String, String> redisTemplate;

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
  public TokenResponse login(LoginRequest request) throws CustomException {
    Users users =
        userRepository
            .findByEmailAndPasswordAndStatus(
                request.getEmail(), request.getPassword(), UserStatus.ACTIVE)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

    log.info("로그인 유저 = {}", users.getName());

    String accessToken = JwtUtil.generateAccessToken(users.getEmail());
    String refreshToken = JwtUtil.generateRefreshToken(users.getEmail());

    redisTemplate.opsForValue().set(users.getEmail(), refreshToken, 7, TimeUnit.DAYS);

    return new TokenResponse(accessToken, refreshToken);
  }

  @Override
  public LogoutResponse logout(LogoutRequest request, HttpServletRequest httpServletRequest)
      throws CustomException {
    String email = httpServletRequest.getAttribute("email").toString();

    Users users =
        userRepository
            .findByEmailAndPasswordAndStatus(email, request.getPassword(), UserStatus.ACTIVE)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

    log.info("로그아웃 유저 = {}", users.getName());

    redisTemplate.delete(email);

    return new LogoutResponse(ErrorCode.OK);
  }

  @Override
  public WithdrawResponse withdraw(WithdrawRequest request, HttpServletRequest httpServletRequest)
      throws CustomException {
    String email = httpServletRequest.getAttribute("email").toString();

    Users user =
        userRepository
            .findByEmailAndPasswordAndStatus(email, request.getPassword(), UserStatus.ACTIVE)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

    user.withdraw();

    redisTemplate.delete(email);

    return new WithdrawResponse(ErrorCode.OK);
  }
}
