package jwt.auth.demo.repository;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import java.util.Optional;
import jwt.auth.demo.entity.Users;
import jwt.auth.demo.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<Users, String> {

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value = "5000")})
  Optional<Users> findByEmail(@Param("email") String email);

  Optional<Users> findByEmailAndPasswordAndStatus(
      @Param("email") String email,
      @Param("password") String password,
      @Param("status") UserStatus status);
}
