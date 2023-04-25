package com.example.springbootshop.dto;

import com.example.springbootshop.constant.DeliveryStatus;
import com.example.springbootshop.constant.OrderStatus;
import com.example.springbootshop.entity.Order;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OrderHistDto {

    public OrderHistDto(Order order) {
        this.orderId = order.getId();
        this.orderDate = order.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.orderStatus = order.getOrderStatus();
        this.deliveryStatus = order.getDeliveryStatus();
    }

    private Long orderId;
    private String orderDate;
    private OrderStatus orderStatus;

    private DeliveryStatus deliveryStatus;

    private List<OrderItemDto> orderItemDtoList = new ArrayList<>();

    public void addOrderItemDto(OrderItemDto orderItemDto) {
        orderItemDtoList.add(orderItemDto);
    }
}
