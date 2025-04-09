package com.template.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "app_users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotEmpty
    private String name;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @EqualsAndHashCode.Exclude
    private Set<Post> posts = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "user_followers",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "follower_id"))
    @EqualsAndHashCode.Exclude
    private Set<User> followers = new HashSet<>();

    @ManyToMany(mappedBy = "followers")
    @EqualsAndHashCode.Exclude
    private Set<User> following = new HashSet<>();
}
