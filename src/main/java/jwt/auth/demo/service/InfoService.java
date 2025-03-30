package jwt.auth.demo.service;

import jakarta.validation.Valid;
import jwt.auth.demo.dto.request.BoardRequest;
import jwt.auth.demo.dto.response.BoardResponse;
import jwt.auth.demo.exception.CustomException;
import org.springframework.transaction.annotation.Transactional;

public interface InfoService {
  @Transactional(readOnly = true)
  BoardResponse getBoards(@Valid BoardRequest request) throws CustomException;
}
