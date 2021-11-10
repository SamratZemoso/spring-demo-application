package com.example.ecommerce.service;

import com.example.ecommerce.model.Cart;
import com.example.ecommerce.model.MyUser;
import com.example.ecommerce.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    CartRepository cartRepository;

    public void saveToCart(Cart cart) {
        cartRepository.save(cart);
    }

    public List<Cart> getAllItems(MyUser user) {
        return cartRepository.findByUser(user);
    }

    public Cart getCartObject(int id) {
        return cartRepository.getById(id);
    }

    public Optional<Cart> getCartItemById(int id) {
        return cartRepository.findById(id);
    }

    public void removeFromCart(Cart cart) {
        cartRepository.delete(cart);
    }

}
