package com.tokkom.product.service;

import com.tokkom.product.dto.request.ProductRequest;
import com.tokkom.product.dto.request.ProductStockRequest;
import com.tokkom.product.dto.response.ProductResponse;
import com.tokkom.product.model.Product;
import com.tokkom.product.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    @Value("${fileUploadRoot}")
    private String fileUploadRoot;

    @Autowired
    private ProductRepository productRepository;

    @Override
    @CacheEvict(value = { "products", "product_title", "product_category", "product", "product_stock" }, allEntries = true)
    public ProductResponse createProduct(ProductRequest productRequest, MultipartFile thumbnail, MultipartFile[] images) {
        Long getDateName = new Date().getTime();

        //rename thumbnail
        String getFileName = FilenameUtils.removeExtension(thumbnail.getOriginalFilename());
        String getFileExt = FilenameUtils.getExtension(thumbnail.getOriginalFilename());
        String newFileName = getFileName + getDateName + "_thumb." + getFileExt;

        String setRootPath = fileUploadRoot + "/" + getDateName;

        //upload thumbnail
        try {
            Path root = Paths.get(setRootPath);
            Files.createDirectories(root);
            Files.copy(thumbnail.getInputStream(), root.resolve(newFileName));
        } catch (IOException e) {
            if (e instanceof FileAlreadyExistsException) {
                throw new RuntimeException("A thumbnail of that name already exists.");
            }
            throw new RuntimeException(e.getMessage());
        }

        List<String> imageNames = new ArrayList<>();

        //upload images
        Arrays.stream(images).forEach(image -> {
                    //rename images
                    String getImageName = FilenameUtils.removeExtension(image.getOriginalFilename());
                    String getImageExt = FilenameUtils.getExtension(image.getOriginalFilename());
                    String newImageName = getImageName + getDateName + "." + getImageExt;
                    imageNames.add(newImageName);
                    try {
                        Path rootImages = Paths.get(setRootPath);
                        Files.createDirectories(rootImages);
                        Files.copy(image.getInputStream(), rootImages.resolve(newImageName));
                    } catch (IOException e) {
                        if (e instanceof FileAlreadyExistsException) {
                            throw new RuntimeException("A Image of that name already exists.");
                        }
                        throw new RuntimeException(e.getMessage());
                    }
                }
        );

        Product product = Product.builder()
                .title(productRequest.getTitle())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .stock(productRequest.getStock())
                .brand(productRequest.getBrand())
                .category(productRequest.getCategory())
                .thumbnail(newFileName)
                .images(imageNames)
                .url(setRootPath)
                .build();
        productRepository.save(product);
        log.info("Product : {} is successfully saved", product.getId());

        return mapToProductResponse(product);
    }

    @Override
    public Resource load(String id, String filename) {
        log.info("Request Product Download from server with id {}", id);
        try {
            Optional<Product> product = productRepository.findById(id);
            Path root = Paths.get(product.get().getUrl());
            Path file = root.resolve(filename);

            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }

        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @Override
    @Cacheable(value = "products")
    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        log.info("getAllProducts successfully retrieved");
        return products.stream().map(this::mapToProductResponse).collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "product_title", key = "#title")
    public List<ProductResponse> getAllProductsByTitle(String title) {
        List<Product> products = productRepository.findByTitleContaining(title);
        log.info("getAllProductsByTitle: {} successfully retrieved", title);
        return products.stream().map(this::mapToProductResponse).collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "product_category", key = "#category")
    public List<ProductResponse> getAllProductsByCategory(String category) {
        List<Product> products = productRepository.findByCategoryContaining(category);
        log.info("getAllProductsByCategory: {} successfully retrieved", category);
        return products.stream().map(this::mapToProductResponse).collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "product", key = "#id")
    public Optional<ProductResponse> getProductById(String id) {
        Optional<Product> product = productRepository.findById(id);
        log.info("getProductById: {} successfully retrieved", id);
        return product.map(this::mapToProductResponse);
    }

    @Override
    @CacheEvict(value = { "products", "product_title", "product_category", "product", "product_stock" }, allEntries = true)
    public void deleteProductById(String id) {
        //get url file directory
        Optional<Product> product = productRepository.findById(id);
        Path root = Paths.get(product.get().getUrl());

        try {
            //delete all files inside directory
            FileSystemUtils.deleteRecursively(root.toFile());
            //delete product from database
            productRepository.deleteById(id);
            log.info("Product with id: {} is successfully deleted", id);
        } catch (Exception e) {
            e.printStackTrace();
            log.warn("Failed Delete Product with id: {}", id);
        }
    }

    @Override
    @Cacheable(value = "product_stock", key = "#id")
    public ProductStockRequest getProductStock(String id) {
        ProductStockRequest productStockRequest = productRepository.findProductStockById(id);
        log.info("getProductStock: {} successfully retrieved", id);
        return productStockRequest;
    }

    @Override
    @CacheEvict(value = { "products", "product_title", "product_category", "product", "product_stock" }, allEntries = true)
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
    @CacheEvict(value = { "products", "product_title", "product_category", "product", "product_stock" }, allEntries = true)
    public ProductResponse updateProduct(String id, ProductRequest productRequest, MultipartFile thumbnail, List<MultipartFile> images) {
        Optional<Product> productData = productRepository.findById(id);
        String productUrl = productData.get().getUrl();
        String newFileName = "";
        String getDirectoryName = productUrl.substring(productUrl.lastIndexOf("/") + 1);
        Long getDateName = new Date().getTime();

        System.out.println("images count : " + images.size());

        List<String> imageNames = new ArrayList<>();

        if (!thumbnail.isEmpty()) {
            Path file = Paths.get(productUrl + "/" + productData.get().getThumbnail());
            try {
                boolean result = Files.deleteIfExists(file);
                if (result) {
                    log.info("Thumbnail is successfully deleted!");
                } else {
                    log.warn("Sorry, unable to delete thumbnail");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            //rename thumbnail
            String getFileName = FilenameUtils.removeExtension(thumbnail.getOriginalFilename());
            String getFileExt = FilenameUtils.getExtension(thumbnail.getOriginalFilename());
            newFileName = getFileName + getDateName + "_thumb." + getFileExt;

            String setRootPath = fileUploadRoot + "/" + getDirectoryName;

            //upload thumbnail
            try {
                Path root = Paths.get(setRootPath);
                Files.copy(thumbnail.getInputStream(), root.resolve(newFileName));
            } catch (IOException e) {
                if (e instanceof FileAlreadyExistsException) {
                    throw new RuntimeException("A thumbnail of that name already exists.");
                }
                throw new RuntimeException(e.getMessage());
            }
        }

        if (!images.get(0).isEmpty()) {
            //delete all existing images
            productData.get().getImages().forEach(image -> {
                        Path imgFile = Paths.get(productUrl + "/" + image);
                        try {
                            boolean result = Files.deleteIfExists(imgFile);
                            if (result) {
                                log.info("Images {} is successfully deleted!", image);
                            } else {
                                log.warn("Sorry, unable to delete images {}", image);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
            );

            //upload new updates images
            images.forEach(image -> {
                        //rename images
                        String getImageName = FilenameUtils.removeExtension(image.getOriginalFilename());
                        String getImageExt = FilenameUtils.getExtension(image.getOriginalFilename());
                        String newImageName = getImageName + getDateName + "." + getImageExt;

                        String setRootPath = fileUploadRoot + "/" + getDirectoryName;

                        imageNames.add(newImageName);

                        try {
                            Path rootImages = Paths.get(setRootPath);
                            Files.copy(image.getInputStream(), rootImages.resolve(newImageName));
                        } catch (IOException e) {
                            if (e instanceof FileAlreadyExistsException) {
                                throw new RuntimeException("A Image of that name already exists.");
                            }
                            throw new RuntimeException(e.getMessage());
                        }
                    }
            );


        }

        if (productData.isPresent()) {
            Product product = productData.get();
            product.setTitle(productRequest.getTitle());
            product.setDescription(productRequest.getDescription());
            product.setPrice(productRequest.getPrice());
            product.setStock(productRequest.getStock());
            product.setBrand(productRequest.getBrand());
            product.setCategory(productRequest.getCategory());

            if (!thumbnail.isEmpty()) {
                product.setThumbnail(newFileName);
            }

            if (!images.get(0).isEmpty()) {
                product.setImages(imageNames);
            }

            log.info("Product with id: {} is successfully updated", id);
            productRepository.save(product);
            return mapToProductResponse(product);
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
                .url(product.getUrl())
                .build();
    }


}
