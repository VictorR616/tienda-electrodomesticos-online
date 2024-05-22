package com.victorrojas.microservicesales.service;

import com.victorrojas.microservicesales.dto.SaleDTO;
import com.victorrojas.microservicesales.dto.ShoppingCartDTO;
import com.victorrojas.microservicesales.model.Sale;

import java.util.List;

public interface ISalesService {

    List<SaleDTO> getSales();

    Sale saveSale(Sale sale);

    void deleteSale(Long id);

    Sale findSale(Long id);

    Sale editSale(Long id, Sale saleEditado);

}
