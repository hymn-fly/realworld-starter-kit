package org.realworld.demo.domain.user;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import org.realworld.demo.domain.base.BaseEntity;
import org.realworld.demo.domain.user.User;

import static com.google.common.base.Preconditions.checkArgument;

@Entity
public class Follow extends BaseEntity {

    @OneToOne
    private User follower;

    @OneToOne
    private User followee;

    /* for table row -> object mapping */
    protected Follow(){}

    public Follow(User follower, User followee){
        checkArgument(followee != null);
        checkArgument(follower != null);

        this.follower = follower;
        this.followee = followee;
    }

}
