package com.ecommerce.microcommerce.web.dao;

import com.ecommerce.microcommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ProductDao extends JpaRepository<Product, Integer> {
   // List<Product> findAll();
    Product findById(int id);
   // Product save(Product product);

    List<Product> findByPrixGreaterThan (int prixLimit);

}
