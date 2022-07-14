package org.realworld.demo.domain.follow.entity;

import static com.google.common.base.Preconditions.checkArgument;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.realworld.demo.domain.base.BaseEntity;
import org.realworld.demo.domain.user.entity.User;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Follow extends BaseEntity {

  @OneToOne
  private User follower;

  @OneToOne
  private User followee;

  public Follow(User follower, User followee) {
    checkArgument(followee != null);
    checkArgument(follower != null);

    this.follower = follower;
    this.followee = followee;
  }

}
