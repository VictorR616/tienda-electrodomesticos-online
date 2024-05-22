package com.victorrojas.microserviceshoppingcart.service;

import com.victorrojas.microserviceshoppingcart.model.ShoppingCart;

import java.util.List;

public interface IShoppingCartService {


    List<ShoppingCart> getShoppingCarts();

    ShoppingCart saveShoppingCart(List<Long> productos);

    void deleteShoppingCart(Long id);

    ShoppingCart findShoppingCart(Long id);

    ShoppingCart editShoppingCart(Long id, List<Long> productos);

    Boolean shoppingCartExistsById(Long id);


}
