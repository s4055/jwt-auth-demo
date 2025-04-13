package jwt.auth.demo.controller;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.*;
import jwt.auth.demo.dto.request.LoginRequest;
import jwt.auth.demo.dto.request.LogoutRequest;
import jwt.auth.demo.dto.request.SignupRequest;
import jwt.auth.demo.dto.request.WithdrawRequest;
import jwt.auth.demo.exception.ErrorCode;
import jwt.auth.demo.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;
  @Autowired private RedisTemplate<String, String> redisTemplate;
  @Autowired private JwtUtil jwtUtil;

  private String accessToken;
  private String refreshToken;
  private static final String email = "user1@example.com";
  private static final String password = "password123!!";

  @BeforeEach
  void setUp() {
    accessToken = jwtUtil.generateAccessToken(email);
    refreshToken = jwtUtil.generateRefreshToken(email);
    redisTemplate.opsForValue().set(email, refreshToken, 7, TimeUnit.DAYS);
  }

  @Rollback(value = true)
  @Test
  void 통합_테스트_회원가입_성공() throws Exception {
    SignupRequest request =
        new SignupRequest("us@example.com", "1111aaaa!!!!", "test", LocalDate.now());

    String content = objectMapper.writeValueAsString(request);

    mockMvc
        .perform(post("/auth/signup").contentType(MediaType.APPLICATION_JSON).content(content))
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(jsonPath("$.resultCode", is(ErrorCode.OK.getResultCode())))
        .andExpect(jsonPath("$.message", is(ErrorCode.OK.getMessage())));
  }

  @Rollback(value = true)
  @Test
  void 통합_테스트_회원가입_중복_이메일() throws Exception {
    SignupRequest request =
        new SignupRequest("user1@example.com", "1111aaaa!!!!", "test", LocalDate.now());

    String content = objectMapper.writeValueAsString(request);

    mockMvc
        .perform(post("/auth/signup").contentType(MediaType.APPLICATION_JSON).content(content))
        .andExpect(status().isConflict())
        .andDo(print())
        .andExpect(jsonPath("$.resultCode", is(ErrorCode.ALREADY_USER.getResultCode())))
        .andExpect(jsonPath("$.message", is(ErrorCode.ALREADY_USER.getMessage())));
  }

  @Test
  void 통합_테스트_로그인_성공() throws Exception {
    LoginRequest request = new LoginRequest("user1@example.com", "password123!!");

    String content = objectMapper.writeValueAsString(request);

    mockMvc
        .perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(content))
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(jsonPath("$.resultCode", is(ErrorCode.OK.getResultCode())))
        .andExpect(jsonPath("$.message", is(ErrorCode.OK.getMessage())));
  }

  @Test
  void 통합_테스트_로그인_아이디_불일치() throws Exception {
    LoginRequest request = new LoginRequest("usr1@example.com", "password123!!");

    String content = objectMapper.writeValueAsString(request);

    mockMvc
        .perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(content))
        .andExpect(status().isNotFound())
        .andDo(print())
        .andExpect(jsonPath("$.resultCode", is(ErrorCode.NOT_FOUND_USER.getResultCode())))
        .andExpect(jsonPath("$.message", is(ErrorCode.NOT_FOUND_USER.getMessage())));
  }

  @Test
  void 통합_테스트_로그아웃_성공() throws Exception {
    LogoutRequest request = new LogoutRequest(password);

    String content = objectMapper.writeValueAsString(request);

    mockMvc
        .perform(
            post("/auth/logout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .header("Authorization", "Bearer " + accessToken))
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(jsonPath("$.resultCode", is(ErrorCode.OK.getResultCode())))
        .andExpect(jsonPath("$.message", is(ErrorCode.OK.getMessage())));
  }

  @Test
  void 통합_테스트_로그아웃_불일치() throws Exception {
    LogoutRequest request = new LogoutRequest("1111aaaa!!!!");

    String content = objectMapper.writeValueAsString(request);

    mockMvc
        .perform(
            post("/auth/logout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .header("Authorization", "Bearer " + accessToken))
        .andExpect(status().isNotFound())
        .andDo(print())
        .andExpect(jsonPath("$.resultCode", is(ErrorCode.NOT_FOUND_USER.getResultCode())))
        .andExpect(jsonPath("$.message", is(ErrorCode.NOT_FOUND_USER.getMessage())));
  }

  @Test
  void 통합_테스트_탈퇴_성공() throws Exception {
    WithdrawRequest request = new WithdrawRequest(password);

    String content = objectMapper.writeValueAsString(request);

    mockMvc
        .perform(
            delete("/auth/withdraw")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .header("Authorization", "Bearer " + accessToken))
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(jsonPath("$.resultCode", is(ErrorCode.OK.getResultCode())))
        .andExpect(jsonPath("$.message", is(ErrorCode.OK.getMessage())));
  }

  @Test
  void 통합_테스트_탈퇴_불일치() throws Exception {
    WithdrawRequest request = new WithdrawRequest("1111aaaa!!!!");

    String content = objectMapper.writeValueAsString(request);

    mockMvc
        .perform(
            delete("/auth/withdraw")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .header("Authorization", "Bearer " + accessToken))
        .andExpect(status().isNotFound())
        .andDo(print())
        .andExpect(jsonPath("$.resultCode", is(ErrorCode.NOT_FOUND_USER.getResultCode())))
        .andExpect(jsonPath("$.message", is(ErrorCode.NOT_FOUND_USER.getMessage())));
  }

  @Test
  void 통합_테스트_리프레시_토큰_재발급_성공() throws Exception {
    mockMvc
        .perform(
            post("/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken)
                .cookie(new Cookie("refreshToken", refreshToken)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.resultCode", is(ErrorCode.OK.getResultCode())))
        .andExpect(jsonPath("$.message", is(ErrorCode.OK.getMessage())))
        .andExpect(jsonPath("$.newAccessToken").exists());
  }

  @Test
  void 통합_테스트_리프레시_토큰_없음() throws Exception {
    mockMvc
        .perform(
            post("/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.resultCode", is(ErrorCode.NO_REFRESH_TOKEN.getResultCode())))
        .andExpect(jsonPath("$.message", is(ErrorCode.NO_REFRESH_TOKEN.getMessage())));
  }

  @Test
  void 통합_테스트_리프레시_토큰_불일치() throws Exception {
    String otherToken = jwtUtil.generateRefreshToken("other@email.com");

    mockMvc
        .perform(
            post("/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken)
                .cookie(new Cookie("refreshToken", otherToken)))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.resultCode", is(ErrorCode.REFRESH_TOKEN_MISMATCH.getResultCode())))
        .andExpect(jsonPath("$.message", is(ErrorCode.REFRESH_TOKEN_MISMATCH.getMessage())));
  }
}
