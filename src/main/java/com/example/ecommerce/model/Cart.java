package com.example.ecommerce.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table
@Data
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String productName;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private MyUser user;

    private double price;

}
