package com.victorrojas.microserviceshoppingcart.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ShoppingCart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Positive
    private Double total_price;
    @ElementCollection(fetch = FetchType.EAGER)
    @NotEmpty(message = "The list of products cannot be empty")
    private List<Long> productsIds;
}
