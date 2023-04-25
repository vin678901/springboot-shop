package com.example.springbootshop.repository;

import com.example.springbootshop.entity.Order;
import com.example.springbootshop.entity.OrderItem;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long>, QuerydslPredicateExecutor<Order>, OrderRepositoryCustom {

    @Query("select o from Order o where o.members.email = :email order by o.createdAt desc")
    List<Order> findOrders(@Param("email") String email, Pageable pageable);

    @Query("select count(o) from Order o where o.members.email = :email")
    Long countOrder(@Param("email") String email);

    //createdBy가 현재 로그인중인 email과 일치하는지 확인.
    @Query("select O from OrderItem O where O.item.createdBy = :createdBy")
    List<OrderItem> findOrderItemsByCreatedBy(@Param("createdBy") String createdBy);
    //OrderItem중 itemId.createdBy가 현재 로그인한 사용자와 같은 OrderItem을 가져온다

    @Query("select o from Order o where o.id = :id")
    Order findOrderById(@Param("id") Long id);
    //findOrderItemsByCreatedBy를 이용해서 OrderItems를 가져오고, OrderItems의 order_id를 이용해서 Order를 가져올 것이다.

}
