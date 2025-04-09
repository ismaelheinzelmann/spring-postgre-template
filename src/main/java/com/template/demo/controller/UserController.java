package com.template.demo.controller;

import com.template.demo.dto.request.UserRequest;
import com.template.demo.dto.response.UserFollowCount;
import com.template.demo.dto.response.UserFollowed;
import com.template.demo.dto.response.UserFollowedCount;
import com.template.demo.dto.response.UserFollowers;
import com.template.demo.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(("/users"))
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> creteUser(@Valid @RequestBody UserRequest userRequest) {
        return userService.createUser(userRequest);
    }

    @PostMapping("/{userId}/follow/{userIdToFollow}")
    public ResponseEntity<Void> follow(
            @PathVariable @NotNull Long userId, @PathVariable @NotNull Long userIdToFollow) {
        return userService.follow(userId, userIdToFollow);
    }

    @GetMapping("/{userId}/followers/count")
    public ResponseEntity<UserFollowCount> followCount(@PathVariable Long userId) {
        return userService.getFollowCount(userId);
    }

    @GetMapping("/{userId}/followers/list")
    public ResponseEntity<UserFollowers> followers(@PathVariable Long userId) {
        return userService.getFollowers(userId);
    }

    @GetMapping("/{userId}/followed/count")
    public ResponseEntity<UserFollowedCount> followedCount(@PathVariable Long userId) {
        return userService.getFollowedCount(userId);
    }

    @GetMapping("/{userId}/followed/list")
    public ResponseEntity<UserFollowed> followed(@PathVariable Long userId) {
        return userService.getFollowed(userId);
    }
}
