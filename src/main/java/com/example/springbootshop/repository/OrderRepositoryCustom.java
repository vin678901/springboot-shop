package com.example.springbootshop.repository;

import com.example.springbootshop.dto.ItemSearchDto;
import com.example.springbootshop.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderRepositoryCustom {
    Page<Item> getOrders(ItemSearchDto itemSearchDto, Pageable pageable);
}
