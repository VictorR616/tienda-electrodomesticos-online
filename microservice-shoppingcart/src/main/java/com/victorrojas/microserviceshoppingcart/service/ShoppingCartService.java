package com.victorrojas.microserviceshoppingcart.service;

import com.netflix.discovery.converters.Auto;
import com.victorrojas.microserviceshoppingcart.dto.ProductDTO;
import com.victorrojas.microserviceshoppingcart.model.ShoppingCart;
import com.victorrojas.microserviceshoppingcart.repository.IProductApiClient;
import com.victorrojas.microserviceshoppingcart.repository.IShoppingCartRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ShoppingCartService implements IShoppingCartService {

    @Autowired
    private IShoppingCartRepository shoppingCartRepository;

    @Autowired
    private IProductApiClient productApiClient;

    @Override
    @CircuitBreaker(name = "microservice-products", fallbackMethod = "fallbackForGetShoppingCarts")
    @Retry(name = "microservice-products")
    public List<ShoppingCart> getShoppingCarts() {
        List<ShoppingCart> shoppingCarts = shoppingCartRepository.findAll();
        List<ProductDTO> products = productApiClient.getAllProducts();

        Map<Long, ProductDTO> productMap = new HashMap<>();
        products.forEach(product -> productMap.put(product.getCode(), product));

        shoppingCarts.forEach(shoppingCart -> {
            List<Long> productIds = shoppingCart.getProductsIds();
            List<Long> listProductIds = new ArrayList<>();

            productIds.forEach(productId -> {
                if (productMap.containsKey(productId)) {
                    listProductIds.add(productId);
                }
            });

        });

        return shoppingCarts;
    }

    @Override
    public ShoppingCart saveShoppingCart(List<Long> productos) {
        List<ProductDTO> products = productApiClient.getAllProducts();
        List<Long> productosIds = new ArrayList<>();
        Double total = 0.0;

        for (ProductDTO product : products) {
            for (Long code : productos) {
                if (code.equals(product.getCode())) {
                    total += product.getPrice();
                    productosIds.add(product.getCode());
                }
            }
        }

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setTotal_price(total);
        shoppingCart.setProductsIds(productosIds);

        return shoppingCartRepository.save(shoppingCart); // Devuelve el carrito guardado
    }

    @Override
    public void deleteShoppingCart(Long id) {
        if (shoppingCartRepository.existsById(id)) {
            shoppingCartRepository.deleteById(id);
        }

    }

    @Override
    public ShoppingCart findShoppingCart(Long id) {
        ShoppingCart shoppingCart = shoppingCartRepository.findById(id).orElse(null);

        if (shoppingCart == null) {
            return null;
        }

        // Llamada a la API de productos
        List<ProductDTO> products = productApiClient.getAllProducts();
        List<Long> listProductIds = new ArrayList<>();
        Map<Long, ProductDTO> productMap = new HashMap<>();

        products.forEach(product -> productMap.put(product.getCode(), product));
        shoppingCart.getProductsIds().forEach(productCode -> {
            if (productMap.containsKey(productCode)) {
                listProductIds.add(productCode);
            }
        });

        ShoppingCart ShoppingCart = new ShoppingCart();
        ShoppingCart.setId(shoppingCart.getId());
        ShoppingCart.setTotal_price(shoppingCart.getTotal_price());
        ShoppingCart.setProductsIds(listProductIds); // Establecer la lista de IDs de productos en lugar de la lista de objetos de productos

        return ShoppingCart;
    }


    @Override
    public ShoppingCart editShoppingCart(Long id, List<Long> productos) {
        ShoppingCart shoppingCart = shoppingCartRepository.findById(id).orElse(null);

        if (shoppingCart == null) {
            return null;
        }

        // Pimero obtenemos todos los productos
        List<ProductDTO> products = productApiClient.getAllProducts();
        List<Long> productosIds = new ArrayList<>();

        Double total = 0.0;

        // Comparamos la id que entregamos en el json con los productos
        for (ProductDTO product : products) {
            for (Long code : productos) {
                if (code.equals(product.getCode())) {
                    total += product.getPrice();
                    productosIds.add(product.getCode());
                }
            }
        }

        shoppingCart.setTotal_price(total);
        shoppingCart.setProductsIds(productosIds);

        return shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public Boolean shoppingCartExistsById(Long id) {
        return shoppingCartRepository.existsById(id);
    }

    // Fallbacks
    public List<ShoppingCart> fallbackForGetShoppingCarts(Throwable trowable) {
        return new ArrayList<>();
    }
    

}
