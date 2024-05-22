package com.victorrojas.microserviceproducts.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.NumberFormat;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long code;
    @Size(min = 3, max = 30, message = "Name must be between 3 and 30 characters long")
    @NotEmpty(message = "Name cannot be empty")
    private String name;
    @Size(min = 3, max = 30, message = "Brand must be between 3 and 30 characters long")
    @NotEmpty(message = "Brand cannot be empty")
    private String brand;
    @NotNull(message = "Price cannot be empty")
    @Positive(message = "Price must be positive")
    @NumberFormat(pattern = "#,##0.00")
    private Double price;

}
