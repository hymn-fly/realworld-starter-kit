package org.realworld.demo.domain.base;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  public Long getId() {
    return id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof BaseEntity)) {
      return false;
    }
    return com.google.common.base.Objects.equal(id, ((BaseEntity) o).id);
  }

  @Override
  public int hashCode() {
    return com.google.common.base.Objects.hashCode(id);
  }
}
