package jwt.auth.demo.repository;

import java.util.Optional;
import jwt.auth.demo.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<Users, String> {

  Optional<Users> findByEmail(@Param("email") String email);

  Optional<Users> findByEmailAndPassword(
      @Param("email") String email, @Param("password") String password);
}
