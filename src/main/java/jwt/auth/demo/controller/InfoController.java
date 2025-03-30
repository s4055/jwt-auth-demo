package jwt.auth.demo.controller;

import jakarta.validation.Valid;
import jwt.auth.demo.dto.request.BoardRequest;
import jwt.auth.demo.dto.response.BoardResponse;
import jwt.auth.demo.exception.CustomException;
import jwt.auth.demo.service.InfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/info")
public class InfoController {

  private final InfoService infoService;

  @GetMapping("/board")
  public ResponseEntity<BoardResponse> getBoards(@ModelAttribute @Valid BoardRequest request)
      throws CustomException {
    BoardResponse response = infoService.getBoards(request);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }
}
