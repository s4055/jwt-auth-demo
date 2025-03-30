package jwt.auth.demo.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LogoutRequest {
  @NotBlank(message = "이메일은 필수 입니다.")
  @Email(message = "이메일 형식이 아닙니다.")
  @Pattern(
      regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$",
      message = "유효한 이메일 주소를 입력하세요.")
  private String email;

  @NotBlank(message = "비밀번호는 필수 입니다.")
  @Pattern(
      regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
      message = "비밀번호는 최소 8자 이상이며, 숫자, 영문자, 특수문자를 포함해야 합니다.")
  private String password;
}
