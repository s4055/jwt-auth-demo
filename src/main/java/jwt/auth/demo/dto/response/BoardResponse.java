package jwt.auth.demo.dto.response;

import java.util.List;
import jwt.auth.demo.dto.common.CommonResponse;
import jwt.auth.demo.entity.Board;
import jwt.auth.demo.exception.ErrorCode;
import lombok.Data;
import lombok.Getter;

@Getter
public class BoardResponse extends CommonResponse {
  List<BoardDto> boardList;

  public BoardResponse(ErrorCode errorCode, List<Board> boards) {
    super(errorCode);
    this.boardList = boards.stream().map(BoardDto::new).toList();
  }

  @Data
  private static class BoardDto {
    private Long id;
    private String title;
    private String writer;

    public BoardDto(Board board) {
      this.id = board.getId();
      this.title = board.getTitle();
      this.writer = board.getWriter();
    }
  }
}
