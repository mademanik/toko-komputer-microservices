package com.tokkom.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tokkom.product.dto.request.ProductRequest;
import com.tokkom.product.dto.request.ProductStockRequest;
import com.tokkom.product.dto.response.ProductResponse;
import com.tokkom.product.dto.response.ProductStockResponse;
import com.tokkom.product.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.core.io.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

            Double currentStock = productStockRequest.getStock();

            ProductStockResponse productStockResponse = new ProductStockResponse();

            if (currentStock >= qty) {
                productStockResponse.setIsProductInStock(true);
                productService.updateProductStock(id, currentStock, qty);
            } else {
                productStockResponse.setIsProductInStock(false);
            }

            return new ResponseEntity<>(productStockResponse, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestPart("product") String product, @RequestPart("thumbnail") MultipartFile thumbnail, @RequestPart("images") MultipartFile[] images) {
        ProductRequest productRequest = new ProductRequest();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            productRequest = objectMapper.readValue(product, ProductRequest.class);

            ProductResponse productResponse = productService.createProduct(productRequest, thumbnail, images);
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
    public ResponseEntity<HttpStatus> deleteProductById(@PathVariable("id") String productId) {
        try {
            productService.deleteProductById(productId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProductById(@PathVariable("id") String id, @RequestPart("product") String product, @RequestPart(name = "thumbnail", required = false) MultipartFile thumbnail, @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        ProductRequest productRequest = new ProductRequest();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            productRequest = objectMapper.readValue(product, ProductRequest.class);

            ProductResponse productResponse = productService.updateProduct(id, productRequest, thumbnail, images);

            return new ResponseEntity<>(productResponse, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
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

    @GetMapping("/download/{id}/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getDownload(@PathVariable String id, @PathVariable String filename) {
        log.info("Download Request: " + filename + " with id " + id);
        Resource file = productService.load(id, filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }


}
