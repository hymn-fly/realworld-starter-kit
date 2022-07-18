package org.realworld.demo.domain.user.entity;

import static com.google.common.base.Preconditions.checkArgument;
import static org.springframework.util.StringUtils.hasText;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.realworld.demo.domain.base.BaseEntity;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

  @Column(unique = true)
  private String email;

  private String password;

  private String username;

  private String bio;

  private String image;

  public void update(String email, String password, String username, String bio, String image) {
    this.email = hasText(email) ? email : this.email;
    this.password = hasText(password) ? password : this.password;
    this.username = hasText(username) ? username : this.username;
    this.bio = hasText(bio) ? bio : this.bio;
    this.image = hasText(image) ? image : this.image;
  }

  public User(String email, String password, String username, String bio, String image) {
    checkArgument(hasText(email), "이메일은 공백일 수 없습니다");
    checkArgument(hasText(password), "비밀번호는 공백일 수 없습니다");
    checkArgument(hasText(username), "유저이름은 공백일 수 없습니다");

    this.email = email;
    this.password = password;
    this.username = username;
    this.bio = bio;
    this.image = image;
  }
}
