package com.tokkom.product.service;

import com.tokkom.product.dto.request.ProductRequest;
import com.tokkom.product.dto.response.ProductResponse;
import com.tokkom.product.models.Product;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ProductService {
    public Product createProduct(ProductRequest productRequest);

    public List<ProductResponse> getAllProducts();

    public List<ProductResponse> getAllProductsByTitle(String title);

    public List<ProductResponse> getAllProductsByCategory(String category);

    public Optional<ProductResponse> getProductById(String id);

    public void deleteProductById(String id);

    public Product updateProduct(String id, ProductRequest product);
}
