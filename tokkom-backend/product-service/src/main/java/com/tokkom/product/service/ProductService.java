package com.tokkom.product.service;

import com.tokkom.product.dto.request.ProductRequest;
import com.tokkom.product.dto.request.ProductStockRequest;
import com.tokkom.product.dto.response.ProductResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    public void cacheInit();

    public ProductResponse createProduct(ProductRequest productRequest, MultipartFile thumbnail, MultipartFile[] images);

    public Resource load(String id, String filename);

    public List<ProductResponse> getAllProducts();

    public List<ProductResponse> getAllProductsByTitle(String title);

    public List<ProductResponse> getAllProductsByCategory(String category);

    public Optional<ProductResponse> getProductById(String id);

    public void deleteProductById(String id);

    public ProductStockRequest getProductStock(String id);

    public Boolean updateProductStock(String id, Double currentStock, Double reqStock);

    public ProductResponse updateProduct(String id, ProductRequest productRequest, MultipartFile thumbnail, List<MultipartFile> images);


}
