package com.example.springbootshop.dto;

import com.example.springbootshop.constant.Category;
import com.example.springbootshop.constant.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemSearchDto {

    private String searchDateType;

    private Category searchCategory;

    private ItemSellStatus searchSellStatus;
    private String searchBy;

    private String searchQuery = "";

    private String createdBy;
}
