package jwt.auth.demo.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import jwt.auth.demo.dto.request.BoardRequest;
import jwt.auth.demo.exception.CustomException;
import jwt.auth.demo.exception.ErrorCode;
import jwt.auth.demo.repository.BoardRepository;
import jwt.auth.demo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class InfoControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private UserRepository userRepository;

  @Autowired private BoardRepository boardRepository;

  @Autowired private ObjectMapper objectMapper;

  @Test
  void 통합_테스트_조회() throws Exception {
    BoardRequest request = new BoardRequest("user1@example.com");

    String content = objectMapper.writeValueAsString(request);

    mockMvc
        .perform(
            get("/info/boards")
                .param("email", request.getEmail())
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(jsonPath("$.resultCode", is(ErrorCode.OK.getResultCode())))
        .andExpect(jsonPath("$.message", is(ErrorCode.OK.getMessage())))
        .andExpect(jsonPath("$.boardList").exists());
  }

  @Test
  void 통합_테스트_조회_이메일_미존재() throws Exception {
    BoardRequest request = new BoardRequest("us@example.com");

    String content = objectMapper.writeValueAsString(request);

    mockMvc
        .perform(
            get("/info/boards")
                .param("email", request.getEmail())
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andDo(print())
        .andExpect(jsonPath("$.resultCode", is(ErrorCode.NOT_FOUND_USER.getResultCode())))
        .andExpect(jsonPath("$.message", is(ErrorCode.NOT_FOUND_USER.getMessage())));
  }
}
