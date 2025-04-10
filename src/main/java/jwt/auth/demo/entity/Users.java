package jwt.auth.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import jwt.auth.demo.dto.request.SignupRequest;
import jwt.auth.demo.enums.UserStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "users")
public class Users extends BaseEntity {

  @Id
  @Column(name = "users_id", length = 36, nullable = false, unique = true)
  private String id;

  @Column(name = "email", length = 100, nullable = false, unique = true, updatable = false)
  private String email;

  @Column(name = "password", length = 18, nullable = false)
  private String password;

  @Column(name = "user_name", length = 20, nullable = false)
  private String name;

  @Column(name = "birth_dt", nullable = false)
  private LocalDate birthDt;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false, length = 10)
  private UserStatus status = UserStatus.ACTIVE;

  @Column(name = "join_dt", nullable = false, updatable = false)
  private LocalDateTime joinDt;

  @Column(name = "withdraw_dt", insertable = false)
  private LocalDateTime withdrawDt;

  @OneToMany(
      mappedBy = "users",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.LAZY)
  List<Board> boardList = new ArrayList<>();

  @PrePersist
  public void generateId() {
    if (this.id == null) {
      this.id = UUID.randomUUID().toString();
    }
  }

  public void withdraw() {
    this.status = UserStatus.WITHDRAWN;
    this.withdrawDt = LocalDateTime.now();
  }

  public Users(SignupRequest request) {
    this.email = request.getEmail();
    this.password = request.getPassword();
    this.name = request.getName();
    this.birthDt = request.getBirthDt();
    this.joinDt = LocalDateTime.now();
  }

  public Users(
      String id,
      String email,
      String password,
      String name,
      LocalDate birthDt,
      UserStatus status,
      LocalDateTime joinDt) {
    this.id = id;
    this.email = email;
    this.password = password;
    this.name = name;
    this.birthDt = birthDt;
    this.status = status;
    this.joinDt = joinDt;
  }
}
