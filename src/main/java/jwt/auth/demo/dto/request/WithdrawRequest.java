package jwt.auth.demo.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WithdrawRequest {
  @NotBlank(message = "이메일은 필수 입니다.")
  @Email(message = "이메일 형식이 아닙니다.")
  @Pattern(
      regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$",
      message = "유효한 이메일 주소를 입력하세요.")
  private String email;
}
