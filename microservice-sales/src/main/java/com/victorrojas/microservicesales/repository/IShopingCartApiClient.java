package com.victorrojas.microservicesales.repository;

import com.victorrojas.microservicesales.dto.ShoppingCartDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "microservice-shoppingcart")
public interface IShopingCartApiClient {

    @GetMapping("/shoppingcart/all")
    List<ShoppingCartDTO> getAllShoppingCarts();
}
