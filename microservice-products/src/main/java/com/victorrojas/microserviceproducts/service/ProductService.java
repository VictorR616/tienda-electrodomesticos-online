package com.victorrojas.microserviceproducts.service;

import com.victorrojas.microserviceproducts.exception.BadRequestException;
import com.victorrojas.microserviceproducts.exception.ResourceNotFoundException;
import com.victorrojas.microserviceproducts.model.Product;
import com.victorrojas.microserviceproducts.repository.IProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService implements IProductService {

    @Autowired
    private IProductRepository productRepository;

    @Override
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    @Override
    public void saveProduct(Product product) {
        productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public Product findProduct(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public Boolean existProduct(Long id) {
        return productRepository.existsById(id);
    }


    @Override
    public Product editProduct(Long id, Product productEditado) {
        Product product = this.findProduct(id);

        product.setName(productEditado.getName());
        product.setBrand(productEditado.getBrand());
        product.setPrice(productEditado.getPrice());
        return productRepository.save(product);
    }
}