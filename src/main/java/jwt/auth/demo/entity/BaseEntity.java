package jwt.auth.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@MappedSuperclass
public abstract class BaseEntity {
  @CreationTimestamp
  @Column(name = "reg_dt", nullable = false, updatable = false)
  private LocalDateTime regDt;

  @UpdateTimestamp
  @Column(name = "mod_dt", insertable = false)
  private LocalDateTime modDt;
}
