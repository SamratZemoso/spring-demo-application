package com.example.ecommerce.service;

import com.example.ecommerce.model.MyUser;
import com.example.ecommerce.model.ProductOrder;
import com.example.ecommerce.repository.ProductOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductOrderService {

    @Autowired
    ProductOrderRepository productOrderRepository;

    public void saveOrder(ProductOrder productOrder) {
        productOrderRepository.save(productOrder);
    }

    public List<ProductOrder> getCartItems(MyUser user) {
        return productOrderRepository.findByUser(user);
    }

}
