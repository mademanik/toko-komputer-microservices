package com.tokkom.product.controllers;

import com.tokkom.product.dto.request.ProductRequest;
import com.tokkom.product.dto.request.ProductStockRequest;
import com.tokkom.product.dto.response.ProductResponse;
import com.tokkom.product.dto.response.ProductStockResponse;
import com.tokkom.product.models.Product;
import com.tokkom.product.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/tokkom/api/product")
@Slf4j
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/stock/{id}/{qty}")
    public ResponseEntity<ProductStockResponse> getProductStock(@PathVariable("id") String id, @PathVariable("qty") Double qty) {
        try {
            ProductStockRequest productStockRequest = productService.getProductStock(id);

            Double stockNumber = productStockRequest.getStock();

            ProductStockResponse productStockResponse = new ProductStockResponse();

            if (stockNumber >= qty) {
                productStockResponse.setIsProductInStock(true);
            } else {
                productStockResponse.setIsProductInStock(false);
            }

            return new ResponseEntity<>(productStockResponse, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest productRequest) {
        try {
            Product product = productService.createProduct(productRequest);

            ProductResponse productResponse = ProductResponse.builder()
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

            return new ResponseEntity<>(productResponse, HttpStatus.CREATED);
        } catch (Exception e) {
            log.info("Error creating product {}", e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<ProductResponse>> getProductById(@PathVariable String id) {
        try {
            Optional<ProductResponse> productResponse = productService.getProductById(id);
            return new ResponseEntity<>(productResponse, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProductById(@PathVariable("id") String productId) {
        try {
            productService.deleteProductById(productId);
            return new ResponseEntity<>("Blog ID: " + productId + " was successfully deleted", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProductById(@PathVariable("id") String id, @RequestBody ProductRequest productRequest) {
        try {
            Product product = productService.updateProduct(id, productRequest);
            ProductResponse productResponse = ProductResponse.builder()
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

            return new ResponseEntity<>(productResponse, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts(@RequestParam(required = false) String title, String category) {

        List<ProductResponse> products = new ArrayList<ProductResponse>();

        try {
            if (title != null) {
                productService.getAllProductsByTitle(title).forEach(products::add);
            }

            if (category != null) {
                productService.getAllProductsByCategory(category).forEach(products::add);
            }

            if (title == null && category == null) {
                productService.getAllProducts().forEach(products::add);
            }

            if (products.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(products, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
