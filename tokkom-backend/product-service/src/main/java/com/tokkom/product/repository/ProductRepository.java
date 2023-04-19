package com.tokkom.product.repository;

import com.tokkom.product.models.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
    List<Product> findByTitleContaining(String title);
    List<Product> findByCategoryContaining(String category);

}
