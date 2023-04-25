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
public class OrderDeliveryDto {
    //해당 판매자가 올린 상품 관련 주문 정보들을 쭉 불러올 것이다.
    public OrderDeliveryDto(Order order) {
        this.orderId = order.getId();
        this.orderDate = order.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.orderStatus = order.getOrderStatus();
        this.deliveryStatus = order.getDeliveryStatus();
        this.customerEmail = order.getMembers().getEmail();
    }

    private Long orderId;
    private String orderDate;
    private OrderStatus orderStatus;

    private DeliveryStatus deliveryStatus;

    private List<OrderItemDto> orderItemDtoList = new ArrayList<>();

    private String customerEmail;

    public void addOrderItemDto(OrderItemDto orderItemDto) {
        orderItemDtoList.add(orderItemDto);
    }
}
