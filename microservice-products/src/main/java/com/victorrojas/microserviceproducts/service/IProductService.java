package com.victorrojas.microserviceproducts.service;

import com.victorrojas.microserviceproducts.model.Product;

import java.util.List;

public interface IProductService {


    List<Product> getProducts();

    void saveProduct(Product product);

    void deleteProduct(Long id);

    Product findProduct(Long id);

    Boolean existProduct(Long id);

    Product editProduct(Long id, Product productEditado);

}
