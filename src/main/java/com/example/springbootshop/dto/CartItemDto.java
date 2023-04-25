package com.example.springbootshop.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Setter
@Getter
public class CartItemDto {

    @NotNull
    private Long itemId;

    @Min(value = 1, message = "1개 이상 담아주세요")
    private int count;
}
