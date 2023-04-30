package org.tokkom.order.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.tokkom.order.constant.AppConstants;
import org.tokkom.order.dto.request.NotificationRequest;
import org.tokkom.order.dto.request.OrderRequest;
import org.tokkom.order.dto.response.OrderResponse;
import org.tokkom.order.dto.response.ProductStockResponse;
import org.tokkom.order.model.Order;
import org.tokkom.order.repository.OrderRepository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Value("${productStockUrl}")
    private String productStockUrl;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    private KafkaTemplate<String, NotificationRequest> kafkaTemplate;

    @Override
    @Transactional
    public OrderResponse createOrder(OrderRequest orderRequest) {

        ProductStockResponse productStockResponse = getProductStock(orderRequest.getProductId(), orderRequest.getProductQty());

        if (productStockResponse.getIsProductInStock() == false) {
            return null;
        }

        Order order = Order.builder()
                .productId(orderRequest.getProductId())
                .productTitle(orderRequest.getProductTitle())
                .productQty(orderRequest.getProductQty())
                .customerName(orderRequest.getCustomerName())
                .customerAddress(orderRequest.getCustomerAddress())
                .customerPhoneNumber(orderRequest.getCustomerPhoneNumber())
                .description(orderRequest.getDescription())
                .build();
        orderRepository.save(order);
        log.info("Order : {} is successfully saved", order.getId());

        NotificationRequest notificationRequest = NotificationRequest.builder()
                .message("New incoming order with product title: " + order.getProductTitle() + " and quantity: " + order.getProductQty().toString())
                .serviceName("order-service")
                .createdAt(new Date())
                .build();

        //send notification via kafka producers
        log.info("Send order detail to kafka: " + notificationRequest.toString());
        kafkaTemplate.send(AppConstants.TOPIC_NAME, notificationRequest);

        return mapToOrderResponse(order);
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream().map(this::mapToOrderResponse).collect(Collectors.toList());
    }

    private OrderResponse mapToOrderResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .productId(order.getProductId())
                .productTitle(order.getProductTitle())
                .productQty(order.getProductQty())
                .customerName(order.getCustomerName())
                .customerAddress(order.getCustomerAddress())
                .customerPhoneNumber(order.getCustomerPhoneNumber())
                .description(order.getDescription()).build();
    }

    @Override
    public Optional<OrderResponse> getOrderById(Long id) {
        Optional<Order> order = orderRepository.findById(id);
        return order.map(this::mapToOrderResponse);
    }

    @Override
    public void deleteOrderById(Long id) {
        orderRepository.deleteById(id);
        log.info("Order with id: {} is successfully deleted", id);

    }

    @Override
    public ProductStockResponse getProductStock(String id, Double stock) {
        String url = productStockUrl + id + "/" + stock;

        ProductStockResponse productStockResponse = webClientBuilder.build()
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(ProductStockResponse.class)
                .block();
        return productStockResponse;
    }
}
