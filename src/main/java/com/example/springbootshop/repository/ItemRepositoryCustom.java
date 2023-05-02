package com.example.springbootshop.repository;

import com.example.springbootshop.dto.ItemSearchDto;
import com.example.springbootshop.dto.MainItemDto;
import com.example.springbootshop.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {
    Page<Item> getSellerItemPage(ItemSearchDto itemSearchDto, Pageable pageable, String createdBy);

    Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable);

}
