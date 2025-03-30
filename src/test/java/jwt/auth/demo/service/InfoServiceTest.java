package jwt.auth.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import jwt.auth.demo.dto.request.BoardRequest;
import jwt.auth.demo.dto.response.BoardResponse;
import jwt.auth.demo.entity.Board;
import jwt.auth.demo.entity.Users;
import jwt.auth.demo.exception.CustomException;
import jwt.auth.demo.exception.ErrorCode;
import jwt.auth.demo.repository.BoardRepository;
import jwt.auth.demo.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class InfoServiceTest {

  @InjectMocks private InfoServiceImpl infoService;

  @Mock private UserRepository userRepository;

  @Mock private BoardRepository boardRepository;

  @Test
  void 조회_사용자_정보_미존재() {
    BoardRequest request = new BoardRequest("u11111@example.com");
    when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

    CustomException exception =
        assertThrows(CustomException.class, () -> infoService.getBoards(request));
    assertEquals(ErrorCode.NOT_FOUND_USER.getResultCode(), exception.getErrorCode());
    assertEquals(ErrorCode.NOT_FOUND_USER.getMessage(), exception.getMessage());

    verify(userRepository, times(1)).findByEmail(request.getEmail());
    verifyNoMoreInteractions(boardRepository);
  }

  @Test
  void 조회_사용자_정보_존재() throws CustomException {
    BoardRequest request = new BoardRequest("u11111@example.com");
    Users user = new Users();

    List<Board> boardList = List.of(new Board(), new Board());

    when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
    when(boardRepository.findByBoards(user.getId())).thenReturn(boardList);

    BoardResponse response = infoService.getBoards(request);

    assertEquals(ErrorCode.OK.getResultCode(), response.getResultCode());
    assertEquals(ErrorCode.OK.getMessage(), response.getMessage());
    assertEquals(2, response.getBoardList().size());

    verify(userRepository, times(1)).findByEmail(request.getEmail());
    verify(boardRepository, times(1)).findByBoards(user.getId());
  }
}
