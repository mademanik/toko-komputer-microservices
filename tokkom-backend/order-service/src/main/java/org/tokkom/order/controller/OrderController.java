package org.tokkom.order.controller;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.tokkom.order.dto.request.OrderRequest;
import org.tokkom.order.dto.response.OrderResponse;
import org.tokkom.order.dto.response.ProductStockResponse;
import org.tokkom.order.service.OrderService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/tokkom/api/order")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    @CircuitBreaker(name = "orderService", fallbackMethod = "fallbackMethod")
    public ResponseEntity<String> getProductStock(@RequestBody OrderRequest orderRequest) {
        OrderResponse orderResponse = orderService.createOrder(orderRequest);

        if (orderResponse == null) {
            return new ResponseEntity<>("Sorry, stock are not available", HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>("Order Request Successfully Created", HttpStatus.CREATED);
    }

    public ResponseEntity<String> fallbackMethod(OrderRequest orderRequest, RuntimeException runtimeException) {
        return new ResponseEntity<>("Oops! Something went wrong, please order after some time!", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
