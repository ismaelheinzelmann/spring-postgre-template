package com.template.demo.model;

import jakarta.persistence.*;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne private User user;

    private Date postDate;

    @OneToOne(cascade = CascadeType.ALL)
    private Product product;

    @Column(nullable = false)
    private int category;

    @Column(nullable = false)
    private long price;
}
