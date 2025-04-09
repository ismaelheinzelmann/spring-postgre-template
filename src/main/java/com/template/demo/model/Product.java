package com.template.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    private Post post;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String color;

    @Column(nullable = false)
    private String notes;
}
