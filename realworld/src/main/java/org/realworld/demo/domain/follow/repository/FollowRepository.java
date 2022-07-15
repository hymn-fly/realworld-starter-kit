package org.realworld.demo.domain.follow.repository;

import org.realworld.demo.domain.follow.entity.Follow;
import org.realworld.demo.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

    List<Follow> findByFollower(User follower);

    List<Follow> findByFollowee(User followee);

    Optional<Follow> findByFollowerAndFollowee(User follower, User followee);
}
