package com.victorrojas.microserviceshoppingcart.controller;

import com.victorrojas.microserviceshoppingcart.exception.BadRequestException;
import com.victorrojas.microserviceshoppingcart.exception.ResourceNotFoundException;
import com.victorrojas.microserviceshoppingcart.model.ShoppingCart;
import com.victorrojas.microserviceshoppingcart.service.IShoppingCartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shoppingcart")
public class ShoppingCartController {

    @Autowired
    private IShoppingCartService shoppingCartService;


    @GetMapping("/all")
    public ResponseEntity<List<ShoppingCart>> getShoppingCarts() {

        List<ShoppingCart> shoppingCarts = shoppingCartService.getShoppingCarts();

        if (shoppingCarts == null || shoppingCarts.isEmpty()) {
            throw new ResourceNotFoundException("The list of shopping carts is empty");
        }

        return new ResponseEntity<>(shoppingCarts, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShoppingCart> findShoppingCart(@PathVariable Long id) {

        ShoppingCart shoppingCart = shoppingCartService.findShoppingCart(id);

        if (shoppingCart == null) {
            throw new ResourceNotFoundException("Shopping cart not found with id: " + id);
        }

        return new ResponseEntity<>(shoppingCart, HttpStatus.OK);

    }

    @PostMapping
    public ResponseEntity<ShoppingCart> saveShoppingCart(@RequestBody @Valid ShoppingCart shoppingCart) {
        try {
            ShoppingCart savedCart = shoppingCartService.saveShoppingCart(shoppingCart.getProductsIds());
            return new ResponseEntity<>(savedCart, HttpStatus.CREATED);
        } catch (DataAccessException e) {
            throw new BadRequestException("Can't save shopping cart" + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ShoppingCart> editShoppingCart(@PathVariable Long id, @RequestBody @Valid ShoppingCart shoppingCartEditado) {
        try {
            if (shoppingCartService.shoppingCartExistsById(id)) {
                ShoppingCart shoppingCart = shoppingCartService.editShoppingCart(id, shoppingCartEditado.getProductsIds());
                return new ResponseEntity<>(shoppingCartService.findShoppingCart(id), HttpStatus.OK);
            } else {
                throw new ResourceNotFoundException("Shopping cart not found with id: " + id);
            }
        } catch (DataAccessException e) {
            throw new BadRequestException("Can't edit shopping cart");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteShoppingCart(@PathVariable Long id) {
        try {
            if (shoppingCartService.shoppingCartExistsById(id)) {
                shoppingCartService.deleteShoppingCart(id);
                return new ResponseEntity<>("Shopping cart deleted", HttpStatus.NO_CONTENT);
            } else {
                throw new ResourceNotFoundException("Shopping cart not found with id: " + id);
            }
        } catch (DataAccessException e) {
            throw new BadRequestException("Can't delete shopping cart");
        }
    }

}
