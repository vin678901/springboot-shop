package com.example.springbootshop.service;

import com.example.springbootshop.constant.ItemSellStatus;
import com.example.springbootshop.dto.CartItemDto;
import com.example.springbootshop.entity.CartItem;
import com.example.springbootshop.entity.Item;
import com.example.springbootshop.entity.Members;
import com.example.springbootshop.repository.CartItemRepository;
import com.example.springbootshop.repository.ItemRepository;
import com.example.springbootshop.repository.MembersRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CartServiceTest {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    MembersRepository membersRepository;

    @Autowired
    CartService cartService;

    @Autowired
    CartItemRepository cartItemRepository;

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
    public void addCart() {
        Item item = saveItem();
        Members members = saveMember();

        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setCount(5);
        cartItemDto.setItemId(item.getId());

        Long cartItemId = cartService.addCart(cartItemDto.getItemId(), cartItemDto.getCount(), members.getEmail());

        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(
                EntityNotFoundException::new);

        assertEquals(item.getId(), cartItem.getItem().getId());
        assertEquals(cartItemDto.getCount(), cartItem.getCount());
    }
}