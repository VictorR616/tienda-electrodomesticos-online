package com.victorrojas.microservicesales.service;

import com.victorrojas.microservicesales.dto.SaleDTO;
import com.victorrojas.microservicesales.dto.ShoppingCartDTO;
import com.victorrojas.microservicesales.exception.ResourceNotFoundException;
import com.victorrojas.microservicesales.model.Sale;
import com.victorrojas.microservicesales.repository.ISalesRepository;
import com.victorrojas.microservicesales.repository.IShopingCartApiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class SalesService implements ISalesService {
    @Autowired
    private IShopingCartApiClient shopingCartApiClient;

    @Autowired
    private ISalesRepository salesRepository;


    @Override
    public List<SaleDTO> getSales() {

        List<Sale> sales = salesRepository.findAll();
        List<ShoppingCartDTO> shoppingCartList = shopingCartApiClient.getAllShoppingCarts();
        List<SaleDTO> saleDTOList = new ArrayList<>();


        sales.forEach(sale -> {
            // Por cada carrito asociar el detalle
            shoppingCartList.forEach(shoppingCartDTO -> {
                if (Objects.equals(shoppingCartDTO.getId(), sale.getShipping_cart_id())) {
                    ShoppingCartDTO shoppingCart = new ShoppingCartDTO();
                    shoppingCart.setId(shoppingCartDTO.getId());
                    shoppingCart.setProductsIds(shoppingCartDTO.getProductsIds());
                    shoppingCart.setTotal_price(shoppingCartDTO.getTotal_price());

                    SaleDTO saleDTO = new SaleDTO();
                    saleDTO.setId(sale.getId());
                    saleDTO.setDate_sale(sale.getDate_sale());
                    saleDTO.setShopping_cart(shoppingCart);
                    saleDTOList.add(saleDTO);
                }
            });
        });

        return saleDTOList;
    }

    @Override
    public Sale saveSale(Sale sale) {

        List<ShoppingCartDTO> shoppingCartList = shopingCartApiClient.getAllShoppingCarts();

        if (shoppingCartList.stream().noneMatch(shoppingCartDTO -> Objects.equals(shoppingCartDTO.getId(), sale.getShipping_cart_id()))) {
            throw new ResourceNotFoundException("The shopping cart with id " + sale.getShipping_cart_id() + " does not exist");
        }

        return salesRepository.save(sale);
    }

    @Override
    public void deleteSale(Long id) {

        Sale sale = this.findSale(id);

        if (sale == null) {
            throw new ResourceNotFoundException("The sale with id " + id + " does not exist");
        }

        salesRepository.deleteById(id);
    }

    @Override
    public Sale findSale(Long id) {
        return salesRepository.findById(id).orElse(null);
    }

    @Override
    public Sale editSale(Long id, Sale saleEditado) {
        Sale sale = this.findSale(id);
        if (sale == null) {
            throw new ResourceNotFoundException("The sale with id " + id + " does not exist");
        }

        List<ShoppingCartDTO> shoppingCartList = shopingCartApiClient.getAllShoppingCarts();
        if (shoppingCartList.stream().noneMatch(shoppingCartDTO -> Objects.equals(shoppingCartDTO.getId(), saleEditado.getShipping_cart_id()))) {
            throw new ResourceNotFoundException("The shopping cart with id " + saleEditado.getShipping_cart_id() + " does not exist");
        }

        sale.setShipping_cart_id(saleEditado.getShipping_cart_id());

        return salesRepository.save(sale);
    }
}
