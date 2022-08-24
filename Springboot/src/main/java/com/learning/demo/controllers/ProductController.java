package com.learning.demo.controllers;

import com.learning.demo.models.Product;
import com.learning.demo.models.ResponseObject;
import com.learning.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/v1/Products")
public class ProductController {

    //DI: Dependency Injection
    @Autowired
    private ProductRepository productRepository;

    @GetMapping("")
        //this request is: http://localhost:8080/api/v1/Products
    ResponseEntity<ResponseObject> getAllProducts() {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Query product successfully", productRepository.findAll())
        );
    }

    //Get detail product
    @GetMapping("/{id}")
    //Let's return an object with: data, message, status,....
    ResponseEntity<ResponseObject> findById(@PathVariable Long id) {
        Optional<Product> foundProduct = productRepository.findById(id);

        return foundProduct.isPresent() ?
                ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("ok", "Query product successfully", foundProduct)
                ) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "Can't find product with id = " + id, "")
        );
    }

    //Insert new product
    //Postman: RAW, Json
    @PostMapping("/insert")
    ResponseEntity<ResponseObject> insertProduct(@RequestBody Product product) {
        //Check product name existed
        List<Product> foundProducts = productRepository.findByProductName(product.getProductName().trim());
        if (foundProducts.size() > 0) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("failed", "Product name already taken", "")
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Insert product successfully", productRepository.save(product))
        );
    }

    //Update, upsert = update if found, otherwise insert
    @PutMapping("{id}")
    ResponseEntity<ResponseObject> updateProduct(@RequestBody Product newProduct, @PathVariable Long id) {
        Product updatedProduct = productRepository.findById(id)
                .map(product -> {
                    product.setProductName(newProduct.getProductName());
                    product.setYear(newProduct.getYear());
                    product.setPrice(newProduct.getPrice());
                    product.setUrl(newProduct.getUrl());
                    return productRepository.save(product);
                }).orElseGet(() -> {
                    newProduct.setId(id);
                    return productRepository.save(newProduct);
                });
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Update product successfully", productRepository.save(updatedProduct))
        );
    }

    //Delete a Product => DELETE method
    @DeleteMapping("/{id}")
    ResponseEntity<ResponseObject> deleteProduct(@PathVariable Long id) {
        boolean exists = productRepository.existsById(id);
        if (exists) {
            productRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Delete product successfully", "")
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "Can't find product to delete", "")
        );
    }
}
