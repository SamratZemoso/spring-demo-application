package com.example.ecommerce.controller;

import com.example.ecommerce.exception.OutOfStockException;
import com.example.ecommerce.model.Cart;
import com.example.ecommerce.model.MyUser;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.model.ProductOrder;
import com.example.ecommerce.service.CartService;
import com.example.ecommerce.service.MyUserDetailsService;
import com.example.ecommerce.service.ProductOrderService;
import com.example.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    MyUserDetailsService myUserDetailsService;

    @Autowired
    ProductService productService;

    @Autowired
    ProductOrderService productOrderService;

    @Autowired
    CartService cartService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JdbcUserDetailsManager jdbcUserDetailsManager;

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    // GET request to register a new User
    @GetMapping("/register")
    public String registerPage(Model model) {

        // add user to model attribute
        model.addAttribute("user", new MyUser());

        // return to registration page
        return "user/register";
    }

    // POST request to register a new User
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user")MyUser user) {

        // list to store the roles of a User
        List<GrantedAuthority> authorities = new ArrayList<>();

        // add roles to the list
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        // encrypt the password
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        User myUser = new User(user.getUsername(), encodedPassword, authorities);

        // save the user with his credentials
        jdbcUserDetailsManager.createUser(myUser);

        // return to login page
        return "redirect:/login";
    }

    // list all the products
    @GetMapping("/list-products")
    public String shopPage(Model model) {

        // get all the products
        List<Product> products = productService.getAllProducts();

        // add products to the model attribute
        model.addAttribute("products", products);

        // return to list of products
        return "user/show-products";
    }

    //
    @GetMapping("/add-to-cart/{id}")
    public String addProduct(@PathVariable("id")int id) {

        Product product = productService.getProductById(id).get();

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String username;

        if(principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        MyUser user = myUserDetailsService.findByUsername(username);

//        System.out.println(product.getProductId());

        Cart cart = new Cart();

        cart.setUser(user);
        cart.setProductName(product.getName());
        cart.setProduct(product);
        cart.setPrice(product.getPrice());

        cartService.saveToCart(cart);

        return "redirect:/cart-items";
    }

    @GetMapping("/cart-items")
    public String cartItems(Model model) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String username;

        if(principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        MyUser user = myUserDetailsService.findByUsername(username);

        List<Cart> cartItems = cartService.getAllItems(user);

        model.addAttribute("cartItems", cartItems);

        return "user/cart";

    }

    @GetMapping("/remove-item/{id}")
    public String removeItem(@PathVariable("id")int id) {

        Cart cart = cartService.getCartObject(id);

        cartService.removeFromCart(cart);

        return "redirect:/cart-items";

    }

    // buy a product using id
    @GetMapping("/buy-product/{id}")
    public String buyProduct(@PathVariable("id")int id) {

        // create an object for ProductOrder
        ProductOrder productOrder = new ProductOrder();

        // get the current logged-in user
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String username;

        if(principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        MyUser user = myUserDetailsService.findByUsername(username);

        Product product = productService.getProductById(id).get();

        // handle out of stock
        try {

            if(product.getStock() <= 0) {
                throw new OutOfStockException("Out of Stock");
            }

            product.decrementStock();

            Date date = new Date();
            Timestamp timestamp = new Timestamp(date.getTime());

            productOrder.setUser(user);
            productOrder.setProduct(product);
            productOrder.setTimestamp(timestamp);
            productOrder.setProductPrice(product.getPrice());

            // save the product order details
            productOrderService.saveOrder(productOrder);

            return "payment";

        } catch (OutOfStockException e) {
            return "redirect:/out-of-stock";
        }
    }

    // list the bought items by user
    @GetMapping("/orders")
    public String getMyItems(Model model) {

        // get the current user
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String username;

        if(principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        MyUser user = myUserDetailsService.findByUsername(username);

        // get all the products of that user
        List<ProductOrder> cart = productOrderService.getCartItems(user);

        // add products to model attribute
        model.addAttribute("myItems", cart);

        return "user/orders";
    }


}
