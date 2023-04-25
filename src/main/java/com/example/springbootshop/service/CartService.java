package com.example.springbootshop.service;

import com.example.springbootshop.dto.CartDetailDto;
import com.example.springbootshop.dto.CartOrderDto;
import com.example.springbootshop.dto.OrderDto;
import com.example.springbootshop.entity.*;
import com.example.springbootshop.repository.CartItemRepository;
import com.example.springbootshop.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class CartService {
    private final ItemService itemService;
    private final MembersService membersService;

    private final CartRepository cartRepository;

    private final CartItemRepository cartItemRepository;

    private final OrderService orderService;

    public Long addCart(Long itemId, int count, String email) {
        Item item = itemService.getItem(itemId);
        Members members = membersService.getMembers(email);

        Cart cart = cartRepository.findByMembersId(members.getId());
        if (cart == null) {
            cart = Cart.createCart(members);
            cartRepository.save(cart);
        }

        CartItem savedCartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(), itemId);

        if (savedCartItem != null) {
            savedCartItem.addCount(count);
            return savedCartItem.getId();
        } else {
            CartItem cartItem = CartItem.createCartItem(cart, item, count);
            cartItemRepository.save(cartItem);
            return cartItem.getId();
        }
    }

    public void deleteCartItem(Long itemId) {
        CartItem cartItem = cartItemRepository.findById(itemId).orElseThrow(
                EntityNotFoundException::new);
        cartItemRepository.delete(cartItem);
    }

    @Transactional(readOnly = true)
    public List<CartDetailDto> getCartList(String email) {

        List<CartDetailDto> cartDetailDtoList = new ArrayList<>();

        Members members = membersService.getMembers(email);
        Cart cart = cartRepository.findByMembersId(members.getId());
        if (cart == null) {
            return cartDetailDtoList;
        }

        cartDetailDtoList = cartItemRepository.findCartDetailDtoList(cart.getId());
        //여기서 문제가

        return cartDetailDtoList;
    }

    @Transactional(readOnly = true)
    public boolean validateCartItem(Long itemId, String email) {
        Members members = membersService.getMembers(email);
        CartItem cartItem = cartItemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);
        Members savedMembers = cartItem.getCart().getMembers();

        if (!StringUtils.equals(members.getEmail(), savedMembers.getEmail()))
            return false;

        return true;
    }

    public void updateCartItemCount(Long cartItemId, int count) {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);
        cartItem.updateCount(count);
    }

    public Long orderCartItem(List<CartOrderDto> cartOrderDtos, String email) {
        List<OrderDto> orderDtoList = new ArrayList<>();
        for (CartOrderDto cartOrderDto : cartOrderDtos) {
            CartItem cartItem = cartItemRepository.findById(cartOrderDto.getCartItemId()).orElseThrow(EntityNotFoundException::new);
            OrderDto orderDto = new OrderDto();
            orderDto.setItemId(cartItem.getItem().getId());
            orderDto.setCount(cartItem.getCount());
            orderDtoList.add(orderDto);
        }

        Long orderId = orderService.orders(orderDtoList, email);

        for (CartOrderDto cartOrderDto : cartOrderDtos) {
            CartItem cartItem = cartItemRepository.findById(cartOrderDto.getCartItemId()).orElseThrow(EntityNotFoundException::new);
            cartItemRepository.delete(cartItem);
        }
        return orderId;
    }

}
