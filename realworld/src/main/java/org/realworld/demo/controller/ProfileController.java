package org.realworld.demo.controller;

import org.realworld.demo.controller.dto.ProfileDto.ProfileResponse;
import org.realworld.demo.domain.user.entity.User;
import org.realworld.demo.domain.follow.service.FollowService;
import org.realworld.demo.domain.user.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profiles")
public class ProfileController {

    private final FollowService stateService;

    private final UserService userService;

    public ProfileController(FollowService stateService, UserService userService) {
        this.stateService = stateService;
        this.userService = userService;
    }

    @GetMapping("/{username}")
    public ProfileResponse getProfile(@PathVariable String username, @AuthenticationPrincipal Object prinicipal){
        User follower = prinicipal instanceof String ? null : (User) prinicipal;
        User followee = userService.getUserByUsername(username);

        boolean following = stateService.checkFollowing(follower, followee);
        return new ProfileResponse(followee.getUsername(), followee.getBio(), followee.getImage(), following);
    }

    @PostMapping("/{username}/follow")
    public ProfileResponse followUser(@PathVariable String username, @AuthenticationPrincipal Object principal){
        User follower = (User) principal;
        User followee = userService.getUserByUsername(username);

        stateService.followUser(follower, followee);

        return new ProfileResponse(followee.getUsername(), followee.getBio(), followee.getImage(), true);
    }

    @DeleteMapping("/{username}/follow")
    public ProfileResponse unfollowUser(@PathVariable String username, @AuthenticationPrincipal Object principal){
        User follower = (User) principal;
        User followee = userService.getUserByUsername(username);

        stateService.unfollowUser(follower, followee);
        return new ProfileResponse(followee.getUsername(), followee.getBio(), followee.getImage(), false);
    }

}
