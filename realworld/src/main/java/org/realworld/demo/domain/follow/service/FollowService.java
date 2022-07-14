package org.realworld.demo.domain.follow.service;

import org.realworld.demo.domain.follow.entity.Follow;
import org.realworld.demo.domain.user.entity.User;
import org.realworld.demo.domain.follow.repository.FollowRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class FollowService {

    private final FollowRepository followRepository;

    public FollowService(FollowRepository followRepository) {
        this.followRepository = followRepository;
    }

    public boolean checkFollowing(User follower, User followee){
        if(follower == null)
            return false;

        Optional<Follow> maybeFollowState = followRepository.findByFollowerAndFollowee(follower, followee);
        return maybeFollowState.isPresent();
    }

    public void followUser(User follower, User followee){
        followRepository.save(new Follow(follower, followee));
    }

    public void unfollowUser(User follower, User followee){
        Optional<Follow> maybeFollowState = followRepository.findByFollowerAndFollowee(follower, followee);
        if(maybeFollowState.isEmpty()){
            return;
        }
        followRepository.deleteById(maybeFollowState.get().getId());
    }
}
