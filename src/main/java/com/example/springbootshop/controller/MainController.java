package com.example.springbootshop.controller;

import com.example.springbootshop.dto.ItemSearchDto;
import com.example.springbootshop.dto.MainItemDto;
import com.example.springbootshop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final ItemService itemService;

    @GetMapping("/")
    public String main(ItemSearchDto itemSearchDto, Optional<Integer> page, Model model) {
//        Pageable pageable = Pageable.ofSize(5);
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6);
        
        Page<MainItemDto> items = itemService.getMainItemPage(itemSearchDto, pageable);
        //여기서 item.imgUrl이 제대로 전달되지 않는 것으로 추정됨
        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 5);

        return "main";
    }


}
