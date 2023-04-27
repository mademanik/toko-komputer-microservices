package com.tokkom.product.service;

import com.tokkom.product.dto.request.ProductRequest;
import com.tokkom.product.dto.request.ProductStockRequest;
import com.tokkom.product.dto.response.ProductResponse;
import com.tokkom.product.model.Product;
import com.tokkom.product.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public ProductResponse createProduct(ProductRequest productRequest) {
        Product product = Product.builder()
                .title(productRequest.getTitle())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .stock(productRequest.getStock())
                .brand(productRequest.getBrand())
                .category(productRequest.getCategory())
                .thumbnail(productRequest.getThumbnail())
                .images(productRequest.getImages())
                .build();
        productRepository.save(product);
        log.info("Product : {} is successfully saved", product.getId());

        return mapToProductResponse(product);
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        log.info("getAllProducts successfully retrieved");
        return products.stream().map(this::mapToProductResponse).collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> getAllProductsByTitle(String title) {
        List<Product> products = productRepository.findByTitleContaining(title);
        log.info("getAllProductsByTitle: {} successfully retrieved", title);
        return products.stream().map(this::mapToProductResponse).collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> getAllProductsByCategory(String category) {
        List<Product> products = productRepository.findByCategoryContaining(category);
        log.info("getAllProductsByCategory: {} successfully retrieved", category);
        return products.stream().map(this::mapToProductResponse).collect(Collectors.toList());
    }

    @Override
    public Optional<ProductResponse> getProductById(String id) {
        Optional<Product> product = productRepository.findById(id);
        log.info("getProductById: {} successfully retrieved", id);
        return product.map(this::mapToProductResponse);
    }

    @Override
    public void deleteProductById(String id) {
        productRepository.deleteById(id);
        log.info("Product with id: {} is successfully deleted", id);
    }

    @Override
    public ProductStockRequest getProductStock(String id) {
        ProductStockRequest productStockRequest = productRepository.findProductStockById(id);
        log.info("getProductStock: {} successfully retrieved", id);
        return productStockRequest;
    }

    @Override
    public Boolean updateProductStock(String id, Double currentStock, Double reqStock) {
        Optional<Product> productData = productRepository.findById(id);

        if (productData.isPresent()) {
            Product product = productData.get();
            product.setStock(currentStock - reqStock);

            log.info("Updated product with id: " + id + ", currentStock: " + currentStock + ", reqStock: " + reqStock);
            log.info("Product Stock with id: {} is successfully updated", id);
            productRepository.save(product);

            return true;
        } else {
            return false;
        }
    }

    @Override
    public Product updateProduct(String id, ProductRequest productRequest) {
        Optional<Product> productData = productRepository.findById(id);
        if (productData.isPresent()) {
            Product product = productData.get();
            product.setTitle(productRequest.getTitle());
            product.setDescription(productRequest.getDescription());
            product.setPrice(productRequest.getPrice());
            product.setStock(productRequest.getStock());
            product.setBrand(productRequest.getBrand());
            product.setCategory(productRequest.getCategory());
            product.setThumbnail(productRequest.getThumbnail());
            product.setImages(productRequest.getImages());

            log.info("Product with id: {} is successfully updated", id);
            return productRepository.save(product);
        } else {
            return null;
        }
    }

    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .title(product.getTitle())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .brand(product.getBrand())
                .category(product.getCategory())
                .thumbnail(product.getThumbnail())
                .images(product.getImages())
                .build();
    }


}
