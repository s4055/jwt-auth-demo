package jwt.auth.demo.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import jwt.auth.demo.dto.common.CommonResponse;
import jwt.auth.demo.exception.ErrorCode;
import jwt.auth.demo.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
public class JwtFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    if (ignoreURI(request)) {
      filterChain.doFilter(request, response);
      return;
    }

    String token = request.getHeader("Authorization");
    if (token == null || !JwtUtil.validateToken(token.replace("Bearer ", ""))) {
      sendErrorResponse(response);
      return;
    }

    String email = JwtUtil.getEmail(token.replace("Bearer ", ""));
    request.setAttribute("email", email);

    filterChain.doFilter(request, response);
  }

  private boolean ignoreURI(HttpServletRequest request) {
    return switch (request.getRequestURI()) {
      case "/auth/signup", "/auth/login" -> true;
      default -> false;
    };
  }

  private void sendErrorResponse(HttpServletResponse response) throws IOException {
    CommonResponse commonResponse = new CommonResponse(ErrorCode.UNAUTHORIZED_TOKEN);
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding(StandardCharsets.UTF_8.name());
    response.getWriter().write(new ObjectMapper().writeValueAsString(commonResponse));
  }
}
