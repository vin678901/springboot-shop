package com.example.springbootshop.controller;

import com.example.springbootshop.dto.OrderDeliveryDto;
import com.example.springbootshop.dto.OrderDto;
import com.example.springbootshop.dto.OrderHistDto;
import com.example.springbootshop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    //ResponseEntity는 HTTP응답을 생성하기 위한 클래스
    @PostMapping("/order")

    public @ResponseBody ResponseEntity order(@RequestBody @Valid OrderDto orderDto, BindingResult bindingResult,
                                              Principal principal) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        Long orderId;
        String email = principal.getName();
        try {
            orderId = orderService.order(orderDto, email);
        } catch (Exception e) {
            return new ResponseEntity<String>("fail", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }

    //order
    @GetMapping(value = {"/orders", "orders/{page}"})
    public String orderHist(@PathVariable("page") Optional<Integer> page, Principal principal, Model model) {
        Pageable pageable = Pageable.ofSize(5);

        Page<OrderHistDto> orderHistDtoList = orderService.getOrderList(principal.getName(), pageable);
        model.addAttribute("orders", orderHistDtoList);
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("maxPage", 5);

        return "order/orderHist";
    }

    @PostMapping("/order/{orderId}/cancel")
    public @ResponseBody ResponseEntity cancelOrder(@PathVariable("orderId") Long orderId, Principal principal) {
        if (!orderService.validateOrder(orderId, principal.getName())) {
            return new ResponseEntity<String>("fail", HttpStatus.FORBIDDEN);
        }
        orderService.cancelOrder(orderId);
        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }

    @GetMapping(value = {"/delivery", "delivery/{page}"})
    public String deliveryHist(@PathVariable("page") Optional<Integer> page, Principal principal, Model model) {
        Pageable pageable = Pageable.ofSize(5);

        Page<OrderDeliveryDto> orderDeliveryDtoList = orderService.getOrderDeliveryList(principal.getName(), pageable);

        model.addAttribute("orders", orderDeliveryDtoList);
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("maxPage", 5);
        return "order/orderDelivery";
    }

    @PostMapping(value = {"/delivery", "delivery/{page}"})
    public String updateDeliveryHist(@PathVariable("page") Optional<Integer> page, Principal principal, Model model) {


        Pageable pageable = Pageable.ofSize(5);

        Page<OrderDeliveryDto> orderDeliveryDtoList = orderService.getOrderDeliveryList(principal.getName(), pageable);

        model.addAttribute("orders", orderDeliveryDtoList);
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("maxPage", 5);
        return "order/orderDelivery";

        //수정해야 된다
    }

    @PostMapping("/delivery/{orderId}/prepare")
    public @ResponseBody ResponseEntity prepareOrder(@PathVariable("orderId") Long orderId, Principal principal) {
        
        orderService.prepareOrder(orderId);
        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }

    @PostMapping("/delivery/{orderId}/ready")
    public @ResponseBody ResponseEntity readyOrder(@PathVariable("orderId") Long orderId, Principal principal) {

        orderService.readyOrder(orderId);
        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }

    @PostMapping("/delivery/{orderId}/complete")
    public @ResponseBody ResponseEntity completeOrder(@PathVariable("orderId") Long orderId, Principal principal) {

        orderService.completeOrder(orderId);
        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }


}


