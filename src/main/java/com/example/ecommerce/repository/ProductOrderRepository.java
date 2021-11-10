package com.example.ecommerce.repository;

import com.example.ecommerce.model.MyUser;
import com.example.ecommerce.model.ProductOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductOrderRepository extends JpaRepository<ProductOrder, Integer> {

    public List<ProductOrder> findByUser(MyUser user);

}
