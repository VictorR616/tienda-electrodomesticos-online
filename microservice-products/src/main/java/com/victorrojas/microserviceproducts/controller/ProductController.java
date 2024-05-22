package com.victorrojas.microserviceproducts.controller;

import com.victorrojas.microserviceproducts.exception.BadRequestException;
import com.victorrojas.microserviceproducts.exception.ResourceNotFoundException;
import com.victorrojas.microserviceproducts.model.Product;
import com.victorrojas.microserviceproducts.service.IProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private IProductService productService;

    @Value("${server.port}")
    private int port;

    @GetMapping("/all")
    public ResponseEntity<List<Product>> listProducts() {
        System.out.println("------Estoy ejecutando el microservicio de products en el puerto: " + port);

        List<Product> products = productService.getProducts();
        if (products == null || products.isEmpty()) {
            throw new ResourceNotFoundException("products");
        }
        return new ResponseEntity<>(products, HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Product> findProduct(@PathVariable Long id) {
        Product product = productService.findProduct(id);
        if (product == null) {
            throw new ResourceNotFoundException("products", "id", id);
        }
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Product> saveProduct(@RequestBody @Valid Product product) {
        try {
            productService.saveProduct(product);
            return new ResponseEntity<>(product, HttpStatus.CREATED);
        } catch (DataAccessException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> editProduct(@PathVariable Long id, @RequestBody @Valid Product productEditado) {
        try {
            if (productService.existProduct(id)) {
                Product product = productService.editProduct(id, productEditado);
                return new ResponseEntity<>(productService.findProduct(id), HttpStatus.OK);
            } else {
                throw new ResourceNotFoundException("products", "id", id);
            }
        } catch (DataAccessException exDt) {
            throw new BadRequestException(exDt.getMessage());
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        try {
            if (productService.existProduct(id)) {
                productService.deleteProduct(id);
                return new ResponseEntity<>("The product has been deleted", HttpStatus.NO_CONTENT);
            } else {
                throw new ResourceNotFoundException("products", "id", id);
            }
        } catch (DataAccessException exDt) {
            throw new BadRequestException(exDt.getMessage());
        }

    }

}


