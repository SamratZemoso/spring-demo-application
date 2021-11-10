package com.example.ecommerce.controller;

import com.example.ecommerce.model.Product;
import com.example.ecommerce.service.ProductService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Controller
public class AdminController {

    @Autowired
    ProductService productService;

    // to trim the whitespaces present
    // at the start and end of the string
    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    // Admin Home page
    @GetMapping("/admin")
    public String adminPage() {
        return "admin/admin-home";
    }

    // List all the products
    @GetMapping("/admin/products")
    public String getProducts(Model model) {

        // add all products to the model
        model.addAttribute("products", productService.getAllProducts());

        return "product/products";
    }


    // add new product
    @GetMapping("/admin/product/add")
    public String addProducts(Model model) {

        model.addAttribute("product", new Product());

        return "product/add-products";
    }


    // POST request to add a new product
    @PostMapping("/admin/product/add")
    public String addProduct(@Valid @ModelAttribute("product") Product product,
                             BindingResult bindingResult) {

        // check whether there are any validation errors
        if(bindingResult.hasErrors()) {

            // redirect to product form with error messages
            return "product/add-products";
        }

        // save the product
        productService.addProduct(product);

        // redirect to list of products
        return "redirect:/admin/products";
    }


    // GET request to update a product by id
    @GetMapping("/admin/product/update/{id}")
    public String updateProduct(@PathVariable("id") int id, Model model) {

        //get product by id
        Optional<Product> product = productService.getProductById(id);

        try {
            if (!product.isPresent()) {
                throw new NotFoundException("Invalid Product ID");
            }

            // add product to model attribute
            model.addAttribute("product", product);

            // return to update product
            return "product/update-product";

        } catch (NotFoundException e) {
            return "redirect:/not-found";
        }
    }


    // POST request to update a product
    @PostMapping("/admin/product/update")
    public String updateProduct(@Valid @ModelAttribute("product")Product product,
                                BindingResult bindingResult) {

        // check if it has any validation errors
        if(bindingResult.hasErrors()) {
            return "product/update-product";
        }

        // update the product
        productService.updateProduct(product);

        // redirect to list of products
        return "redirect:/admin/products";

    }

}
