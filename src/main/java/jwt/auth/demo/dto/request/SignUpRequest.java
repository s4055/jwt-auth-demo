package jwt.auth.demo.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignupRequest {
  @Schema(example = "test@gmail.com")
  @NotBlank(message = "이메일은 필수 입니다.")
  @Email(message = "이메일 형식이 아닙니다.")
  @Pattern(
      regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$",
      message = "유효한 이메일 주소를 입력하세요.")
  private String email;

  @Schema(example = "111aaa!!!")
  @NotBlank(message = "비밀번호는 필수 입니다.")
  @Pattern(
      regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
      message = "비밀번호는 최소 8자 이상이며, 숫자, 영문자, 특수문자를 포함해야 합니다.")
  private String password;

  @Schema(example = "테스트 유저")
  @NotBlank(message = "이름은 필수 입니다.")
  private String name;

  @Schema(example = "2024-01-01")
  @NotNull(message = "생년월인은 필수 입니다.")
  private LocalDate birthDt;
}
