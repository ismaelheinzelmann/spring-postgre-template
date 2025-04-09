package com.template.demo.service;

import com.template.demo.dto.request.UserRequest;
import com.template.demo.dto.response.*;
import com.template.demo.model.User;
import com.template.demo.repository.UserRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public ResponseEntity<Void> createUser(@Valid UserRequest userRequest) {
        User user = new User();
        user.setName(userRequest.name());
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<UserFollowCount> getFollowCount(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(
                new UserFollowCount(
                        user.get().getId(), user.get().getName(), user.get().getFollowers().size()));
    }

    public ResponseEntity<UserFollowedCount> getFollowedCount(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(
                new UserFollowedCount(
                        user.get().getId(), user.get().getName(), user.get().getFollowing().size()));
    }

    public ResponseEntity<UserFollowers> getFollowers(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(
                new UserFollowers(
                        user.get().getId(),
                        user.get().getName(),
                        user.get().getFollowers().stream()
                                .map(
                                        userInformation ->
                                                new UserInformation(userInformation.getId(), userInformation.getName()))
                                .toList()));
    }

    public ResponseEntity<UserFollowed> getFollowed(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(
                new UserFollowed(
                        user.get().getId(),
                        user.get().getName(),
                        user.get().getFollowing().stream()
                                .map(
                                        userInformation ->
                                                new UserInformation(userInformation.getId(), userInformation.getName()))
                                .toList()));
    }

    @Transactional
    public ResponseEntity<Void> follow(@NotNull Long userId, @NotNull Long userIdToFollow) {
        if (Objects.equals(userId, userIdToFollow)) {
            return ResponseEntity.badRequest().build();
        }
        Optional<User> user = userRepository.findById(userId);
        Optional<User> userToFollow = userRepository.findById(userIdToFollow);
        if (user.isEmpty() || userToFollow.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        user.get().getFollowing().add(userToFollow.get());
        userToFollow.get().getFollowers().add(user.get());
        userRepository.save(user.get());
        userRepository.save(userToFollow.get());
        return ResponseEntity.ok().build();
    }
}
