package com.victorrojas.microservicesales.repository;

import com.victorrojas.microservicesales.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISalesRepository extends JpaRepository<Sale, Long> {
}
