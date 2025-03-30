package jwt.auth.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "board")
public class Board extends BaseEntity {

  @Id
  @Column(name = "board_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "title", length = 30, nullable = false)
  private String title;

  @Column(name = "contents", length = 3000, nullable = false)
  private String contents;

  @Column(name = "writer", length = 20, nullable = false)
  private String writer;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "users_id", nullable = false)
  private Users users;
}
