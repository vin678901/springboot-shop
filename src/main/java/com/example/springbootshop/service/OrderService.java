package com.example.springbootshop.service;

import com.example.springbootshop.dto.OrderDeliveryDto;
import com.example.springbootshop.dto.OrderDto;
import com.example.springbootshop.dto.OrderHistDto;
import com.example.springbootshop.dto.OrderItemDto;
import com.example.springbootshop.entity.*;
import com.example.springbootshop.repository.ItemImgRepository;
import com.example.springbootshop.repository.ItemRepository;
import com.example.springbootshop.repository.MembersRepository;
import com.example.springbootshop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final ItemRepository itemRepository;
    private final MembersRepository membersRepository;
    private final OrderRepository orderRepository;

    private final ItemImgRepository itemImgRepository;

    public Long order(OrderDto orderDto, String email) {
        Item item = itemRepository.findById(orderDto.getItemId()).orElseThrow(EntityNotFoundException::new);
        Members members = membersRepository.findByEmail(email);

        List<OrderItem> orderItemList = new ArrayList<>();
        orderItemList.add(OrderItem.createOrderItem(item, orderDto.getCount()));

        Order order = Order.createOrder(members, orderItemList);
        orderRepository.save(order);

        return order.getId();
    }

    @Transactional(readOnly = true)
    public Page<OrderHistDto> getOrderList(String email, Pageable pageable) {
//orderItemDtoList확인
        List<Order> orders = orderRepository.findOrders(email, pageable);
        Long totalCount = orderRepository.countOrder(email);

        List<OrderHistDto> orderHistDtos = new ArrayList<>();

        for (Order order : orders) {
            OrderHistDto orderHistDto = new OrderHistDto(order);
            List<OrderItem> orderItems = order.getOrderItemList();
            for (OrderItem orderItem : orderItems) {
                ItemImg itemImg = itemImgRepository.findByItemIdAndRepimgYn(orderItem.getItem().getId(), "Y");
                OrderItemDto orderItemDto = new OrderItemDto(orderItem, itemImg.getImgUrl());
                orderHistDto.addOrderItemDto(orderItemDto); // 이 부분
            }

            orderHistDtos.add(orderHistDto);
        }

        return new PageImpl<OrderHistDto>(orderHistDtos, pageable, totalCount);
    }

    @Transactional(readOnly = true)
    public boolean validateOrder(Long orderId, String email) {

        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
        Members members = membersRepository.findByEmail(email);

        Members savedMember = order.getMembers();

        if (!StringUtils.equals(savedMember.getEmail(), members.getEmail())) {
            return false;
        }

        return true;
    }

    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
        order.cancelOrder();
    }

    @Transactional(readOnly = true)
    public Page<OrderDeliveryDto> getOrderDeliveryList(String createdBy, Pageable pageable) {
        List<OrderItem> orderItemList = orderRepository.findOrderItemsByCreatedBy(createdBy);
        List<Order> orderList = new ArrayList<>();
        List<OrderDeliveryDto> orderDeliveryDtoList = new ArrayList<>();
        Long totalCount = orderRepository.countOrder(createdBy);


        for (OrderItem orderItem : orderItemList) {
            Order order = orderRepository.findOrderById(orderItem.getOrder().getId());
            orderList.add(order);
            OrderDeliveryDto orderDeliveryDto = new OrderDeliveryDto(order);


            for (OrderItem orderItem2 : order.getOrderItemList()) {
                ItemImg itemImg = itemImgRepository.findByItemIdAndRepimgYn(orderItem2.getItem().getId(), "Y");
                OrderItemDto orderItemDto = new OrderItemDto(orderItem2, itemImg.getImgUrl());
                orderDeliveryDto.addOrderItemDto(orderItemDto);
            }
            orderDeliveryDtoList.add(orderDeliveryDto);
        }

        return new PageImpl<OrderDeliveryDto>(orderDeliveryDtoList, pageable, totalCount);
    }

    public void prepareOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
        order.prepareDelivery();
    }

    public void readyOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
        order.readyDelivery();
    }

    public void completeOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
        order.completeDelivery();
    }

    public Long orders(List<OrderDto> orderDtoList, String email) {
        Members members = membersRepository.findByEmail(email);
        List<OrderItem> orderItemList = new ArrayList<>();

        for (OrderDto orderDto : orderDtoList) {
            Item item = itemRepository.findById(orderDto.getItemId()).orElseThrow(EntityNotFoundException::new);
            orderItemList.add(OrderItem.createOrderItem(item, orderDto.getCount()));
        }

        Order order = Order.createOrder(members, orderItemList);
        orderRepository.save(order);

        return order.getId();
    }
}

