package org.tokkom.order.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import org.tokkom.order.dto.request.OrderRequest;
import org.tokkom.order.dto.response.OrderResponse;
import org.tokkom.order.dto.response.ProductStockResponse;
import org.tokkom.order.service.OrderService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tokkom/api/order")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/{id}")
    @CircuitBreaker(name = "orderService")
    public ResponseEntity<?> createOrder(@PathVariable String id, @RequestParam("order") String order) {
        OrderRequest orderRequest = new OrderRequest();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            orderRequest = objectMapper.readValue(order, OrderRequest.class);

            OrderResponse orderResponse = orderService.createOrder(orderRequest);

            if (orderResponse == null) {
                return new ResponseEntity<>("Sorry, stock are not available", HttpStatus.FORBIDDEN);
            }

            return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
        } catch (Exception e) {
            log.info("Error creating order {}", e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        try {
            List<OrderResponse> orders = orderService.getAllOrders();
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<OrderResponse>> getOrderById(@PathVariable Long id) {
        try {
            Optional<OrderResponse> orderResponse = orderService.getOrderById(id);
            return new ResponseEntity<>(orderResponse, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public ResponseEntity<String> fallbackMethod(OrderRequest orderRequest, RuntimeException runtimeException) {
        return new ResponseEntity<>("Oops! Something went wrong, please order after some time!", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
