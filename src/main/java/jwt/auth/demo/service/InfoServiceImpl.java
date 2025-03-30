package jwt.auth.demo.service;

import jwt.auth.demo.dto.request.BoardRequest;
import jwt.auth.demo.dto.response.BoardResponse;
import jwt.auth.demo.entity.Board;
import jwt.auth.demo.entity.Users;
import jwt.auth.demo.exception.CustomException;
import jwt.auth.demo.exception.ErrorCode;
import jwt.auth.demo.repository.BoardRepository;
import jwt.auth.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class InfoServiceImpl implements InfoService {

  private final UserRepository userRepository;
  private final BoardRepository boardRepository;

  @Override
  public BoardResponse getBoards(BoardRequest request) throws CustomException {
    Users user =
        userRepository
            .findByEmail(request.getEmail())
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

    List<Board> boards = boardRepository.findByBoards(user.getId());
    return new BoardResponse(ErrorCode.OK, boards);
  }
}
