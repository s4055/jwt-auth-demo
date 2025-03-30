package jwt.auth.demo.controller;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import jwt.auth.demo.dto.request.LoginRequest;
import jwt.auth.demo.dto.request.LogoutRequest;
import jwt.auth.demo.dto.request.SignUpRequest;
import jwt.auth.demo.dto.request.WithdrawRequest;
import jwt.auth.demo.exception.ErrorCode;
import jwt.auth.demo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private UserRepository userRepository;

  @Autowired private ObjectMapper objectMapper;

  @Rollback(value = true)
  @Test
  void 통합_테스트_회원가입() throws Exception {
    SignUpRequest request =
        new SignUpRequest("us@example.com", "1111aaaa!!!!", "test", LocalDate.now());

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
  void 통합_테스트_회원가입_이메일_존재() throws Exception {
    SignUpRequest request =
        new SignUpRequest("user1@example.com", "1111aaaa!!!!", "test", LocalDate.now());

    String content = objectMapper.writeValueAsString(request);

    mockMvc
        .perform(post("/auth/signup").contentType(MediaType.APPLICATION_JSON).content(content))
        .andExpect(status().isConflict())
        .andDo(print())
        .andExpect(jsonPath("$.resultCode", is(ErrorCode.ALREADY_USER.getResultCode())))
        .andExpect(jsonPath("$.message", is(ErrorCode.ALREADY_USER.getMessage())));
  }

  @Test
  void 통합_테스트_로그인() throws Exception {
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
  void 통합_테스트_로그인_불일치() throws Exception {
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
  void 통합_테스트_로그아웃() throws Exception {
    LogoutRequest request = new LogoutRequest("user1@example.com", "password123!!");

    String content = objectMapper.writeValueAsString(request);

    mockMvc
        .perform(post("/auth/logout").contentType(MediaType.APPLICATION_JSON).content(content))
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(jsonPath("$.resultCode", is(ErrorCode.OK.getResultCode())))
        .andExpect(jsonPath("$.message", is(ErrorCode.OK.getMessage())));
  }

  @Test
  void 통합_테스트_로그아웃_불일치() throws Exception {
    LogoutRequest request = new LogoutRequest("usr1@example.com", "password123!!");

    String content = objectMapper.writeValueAsString(request);

    mockMvc
        .perform(post("/auth/logout").contentType(MediaType.APPLICATION_JSON).content(content))
        .andExpect(status().isNotFound())
        .andDo(print())
        .andExpect(jsonPath("$.resultCode", is(ErrorCode.NOT_FOUND_USER.getResultCode())))
        .andExpect(jsonPath("$.message", is(ErrorCode.NOT_FOUND_USER.getMessage())));
  }

  @Test
  void 통합_테스트_탈퇴() throws Exception {
    WithdrawRequest request = new WithdrawRequest("user1@example.com");

    String content = objectMapper.writeValueAsString(request);

    mockMvc
        .perform(delete("/auth/delete").contentType(MediaType.APPLICATION_JSON).content(content))
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(jsonPath("$.resultCode", is(ErrorCode.OK.getResultCode())))
        .andExpect(jsonPath("$.message", is(ErrorCode.OK.getMessage())));
  }

  @Test
  void 통합_테스트_탈퇴_불일치() throws Exception {
    WithdrawRequest request = new WithdrawRequest("usr1@example.com");

    String content = objectMapper.writeValueAsString(request);

    mockMvc
        .perform(delete("/auth/delete").contentType(MediaType.APPLICATION_JSON).content(content))
        .andExpect(status().isNotFound())
        .andDo(print())
        .andExpect(jsonPath("$.resultCode", is(ErrorCode.NOT_FOUND_USER.getResultCode())))
        .andExpect(jsonPath("$.message", is(ErrorCode.NOT_FOUND_USER.getMessage())));
  }
}
