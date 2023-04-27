package org.tokkom.order.service;

import org.springframework.stereotype.Service;
import org.tokkom.order.dto.request.OrderRequest;
import org.tokkom.order.dto.response.OrderResponse;
import org.tokkom.order.dto.response.ProductStockResponse;

import java.util.List;
import java.util.Optional;

@Service
public interface OrderService {
    public OrderResponse createOrder(OrderRequest orderRequest);

    public List<OrderResponse> getAllOrders();

    public Optional<OrderResponse> getOrderById(Long id);

    public void deleteOrderById(Long id);

    public ProductStockResponse getProductStock(String id, Double stock);
}