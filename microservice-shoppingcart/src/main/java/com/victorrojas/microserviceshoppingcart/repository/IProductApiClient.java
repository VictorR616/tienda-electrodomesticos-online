package com.victorrojas.microserviceshoppingcart.repository;

import com.victorrojas.microserviceshoppingcart.dto.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "microservice-products" )
public interface IProductApiClient {

    @GetMapping("/products/all")
    List<ProductDTO> getAllProducts();

}
