package com.example.ecommerce.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product")
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private int productId;

    @Column
    @NotEmpty(message = "is required")
    private String name;

    @Column
    @NotNull(message = "is required")
    @Min(value = 0, message = "cant be negative")
    double price;

    @Column
    @NotEmpty(message = "is required")
    private String description;

    @Column
    @Min(value = 0)
    @Max(value = 100)
    @NotNull(message = "is required")
    Integer stock;

    @OneToMany(mappedBy = "product")
    private List<ProductOrder> productOrder = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    private List<Cart> productList = new ArrayList<>();

    public void decrementStock() {
        setStock(getStock() - 1);
    }

}
