package com.victorrojas.microservicesales.controller;

import com.victorrojas.microservicesales.dto.SaleDTO;
import com.victorrojas.microservicesales.exception.BadRequestException;
import com.victorrojas.microservicesales.exception.ResourceNotFoundException;
import com.victorrojas.microservicesales.model.Sale;
import com.victorrojas.microservicesales.service.ISalesService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sales")
public class SalesController {

    @Autowired
    private ISalesService salesService;

    @GetMapping("/all")
    public ResponseEntity<List<SaleDTO>> getAllSales() {
        List<SaleDTO> sales = salesService.getSales();

        if (sales == null || sales.isEmpty()) {
            throw new ResourceNotFoundException("The list of sales is empty");
        }

        return new ResponseEntity<>(sales, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sale> getSale(@PathVariable Long id) {
        Sale sale = salesService.findSale(id);

        if (sale == null) {
            throw new ResourceNotFoundException("The sale with id " + id + " does not exist");
        }

        return new ResponseEntity<>(sale, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Sale> saveSale(@RequestBody @Valid Sale sale) {
        try {
            Sale savedSale = salesService.saveSale(sale);
            return new ResponseEntity<>(savedSale, HttpStatus.CREATED);
        } catch (DataAccessException e) {
            throw new BadRequestException("Can't save sale" + e.getMessage());
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<Sale> updateSale(@PathVariable Long id, @RequestBody @Valid Sale sale) {
        try {
            Sale updatedSale = salesService.editSale(id, sale);
            return new ResponseEntity<>(updatedSale, HttpStatus.OK);
        } catch (DataAccessException e) {
            throw new BadRequestException("Can't update sale" + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSale(@PathVariable Long id) {
        try {
            salesService.deleteSale(id);
            return new ResponseEntity<>("The sale was deleted successfully", HttpStatus.NO_CONTENT);
        } catch (DataAccessException e) {
            throw new BadRequestException("Can't delete sale" + e.getMessage());
        }
    }


}
