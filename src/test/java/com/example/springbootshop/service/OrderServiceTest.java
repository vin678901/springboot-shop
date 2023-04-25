package com.example.springbootshop.service;

import com.example.springbootshop.constant.ItemSellStatus;
import com.example.springbootshop.dto.OrderDto;
import com.example.springbootshop.entity.Item;
import com.example.springbootshop.entity.Members;
import com.example.springbootshop.entity.Order;
import com.example.springbootshop.entity.OrderItem;
import com.example.springbootshop.repository.ItemRepository;
import com.example.springbootshop.repository.MembersRepository;
import com.example.springbootshop.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    MembersRepository membersRepository;

    public Item saveItem() {
        Item item = new Item();
        item.setItemName("test");
        item.setPrice(10000);
        item.setItemDetail("test");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        return itemRepository.save(item);
    }

    public Members saveMember() {
        Members members = new Members();
        members.setEmail("testt@test.com");
        members.setName("test");
        members.setPassword("XXXX");
        return membersRepository.save(members);
    }

    @Test
    public void order() {
        Item item = saveItem();
        Members members = saveMember();
        OrderDto orderDto = new OrderDto();
        orderDto.setCount(10);
        orderDto.setItemId(item.getId());

        System.out.println("******************************");
        System.out.println(members.toString());
        System.out.println(members.toString());
        System.out.println(members.toString());
        System.out.println(members.toString());
        System.out.println(members.toString());
        System.out.println(members.toString());
        System.out.println("******************************");
        Long orderId = orderService.order(orderDto, members.getEmail());

        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);

        List<OrderItem> orderItems = order.getOrderItemList();

        int totalPrice = orderDto.getCount() * item.getPrice();

        assertEquals(totalPrice, order.getTotalPrice());
    }

    @Test
    public void findByEmailTest() {
        Item item = saveItem();
        Members members = saveMember();
        OrderDto orderDto = new OrderDto();
        orderDto.setCount(10);
        orderDto.setItemId(item.getId());


        System.out.println("******************************");
        System.out.println(membersRepository.findByEmail(members.getEmail()));
        System.out.println(membersRepository.findByEmail(members.getEmail()));
        System.out.println(membersRepository.findByEmail(members.getEmail()));
        System.out.println(membersRepository.findByEmail(members.getEmail()));
        System.out.println(membersRepository.findByEmail(members.getEmail()));
        System.out.println(membersRepository.findByEmail(members.getEmail()));
        System.out.println(membersRepository.findByEmail(members.getEmail()));
        System.out.println(membersRepository.findByEmail(members.getEmail()));
        System.out.println("******************************");

    }

    @Test
    public void cancelOrder() {
        Item item = saveItem();
        Members members = saveMember();

        OrderDto orderDto = new OrderDto();
        orderDto.setCount(10);
        orderDto.setItemId(item.getId());
        Long orderId = orderService.order(orderDto, members.getEmail());


        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
        orderService.cancelOrder(orderId);


        assertEquals(ItemSellStatus.SELL, item.getItemSellStatus());
        assertEquals(100, item.getStockNumber());
    }
}