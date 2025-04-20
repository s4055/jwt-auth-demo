# 프로젝트 개요
- JWT 토큰 Demo 프로젝트

# 개발 방향
- 회원가입, 로그인, 로그아웃, 회원탈퇴, 토큰 재발급 기능 개발
- 점진적으로 개선해나가는 방식으로 구현
  - JWT 토큰 없는 구조 개발
  - JWT 토큰 적용
    - JwtFilter 에서 JWT 토큰 검사 로직 추가
  - Refresh 토큰 적용
    - 레디스를 적용하여 DB 부하 감소
  - Spring Security 적용

# 테스트
- swagger
  - http://localhost:8080/swagger-ui/index.html
- h2
  - http://localhost:8080/h2-console/