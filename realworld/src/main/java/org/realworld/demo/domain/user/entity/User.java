package org.realworld.demo.domain.user.entity;

import static com.google.common.base.Preconditions.checkArgument;
import static org.springframework.util.StringUtils.hasText;

import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.realworld.demo.domain.base.BaseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity implements UserDetails {

  @Column(unique = true)
  private String email;

  private String password;

  private String username;

  private String bio;

  private String image;

  public User update(String email, String password, String username, String bio, String image) {
    if (hasText(email)) {
      this.email = email;
    }
    if (hasText(password)) {
      this.password = password;
    }
    if (hasText(username)) {
      this.username = username;
    }
    if (hasText(bio)) {
      this.bio = bio;
    }
    if (hasText(image)) {
      this.image = image;
    }
    return this;
  }

  public User(String email, String password, String username, String bio, String image) {
    checkArgument(hasText(email));
    checkArgument(hasText(password));
    checkArgument(hasText(username));

    this.email = email;
    this.password = password;
    this.username = username;
    this.bio = bio;
    this.image = image;
  }


  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return new ArrayList<>();
  }

  @Override
  public boolean isAccountNonExpired() {
    return false;
  }

  @Override
  public boolean isAccountNonLocked() {
    return false;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return false;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

}
