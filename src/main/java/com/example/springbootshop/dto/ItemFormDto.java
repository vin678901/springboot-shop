package com.example.springbootshop.dto;

import com.example.springbootshop.constant.Category;
import com.example.springbootshop.constant.ItemSellStatus;
import com.example.springbootshop.entity.Item;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ItemFormDto {

    private Long id;

    @NotBlank(message = "상품명을 입력해 주세요.")
    private String itemName;

    @NotNull(message = "가격을 입력해 주세요.")
    private Integer price;

    @NotBlank(message = "상품 설명을 입력해 주세요.")
    private String itemDetail;

    @NotNull(message = "재고 수를 입력해 주세요.")
    private Integer stockNumber;

    @NotNull(message = "카테고리를 선택해 주세요")
    private Category category;

    private ItemSellStatus itemSellStatus;

    private List<ItemImgDto> itemImgDtoList = new ArrayList<>();

    private List<Long> itemImgIds = new ArrayList<>();

    private static ModelMapper modelMapper = new ModelMapper();

    public Item createItem() {
        return modelMapper.map(this, Item.class);
    }

    public static ItemFormDto of(Item item) {
        return modelMapper.map(item, ItemFormDto.class);
    }

}
