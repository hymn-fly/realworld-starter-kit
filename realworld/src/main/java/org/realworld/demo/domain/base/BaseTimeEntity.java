package org.realworld.demo.domain.base;

import com.google.common.base.Objects;
import java.time.LocalDateTime;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private LocalDateTime createdAt;

  protected LocalDateTime updatedAt;

  public Long getId() {
    return id;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  protected BaseTimeEntity() {
    createdAt = LocalDateTime.now();
    updatedAt = LocalDateTime.now();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof BaseTimeEntity)) {
      return false;
    }
    BaseTimeEntity that = (BaseTimeEntity) o;
    return Objects.equal(id, that.id) && Objects.equal(createdAt, that.createdAt) && Objects.equal(
        updatedAt, that.updatedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id, createdAt, updatedAt);
  }
}
