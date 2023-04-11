package com.example.springbootshop.controller;

import com.example.springbootshop.dto.MembersFormDto;
import com.example.springbootshop.dto.MembersUpdateDto;
import com.example.springbootshop.entity.Members;
import com.example.springbootshop.service.MembersService;
import com.example.springbootshop.vo.Address;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;
import java.security.Principal;

@RequestMapping("/members")
@Controller
@RequiredArgsConstructor
public class MembersController {
    private final MembersService membersService;

    private final PasswordEncoder passwordEncoder;

    @PersistenceContext
    EntityManager em;

    @GetMapping("/new")
    public String getNewMembers(Model model) {
        model.addAttribute("membersFormDto", new MembersFormDto());
        return "members/membersForm";
    }

    @PostMapping("/new")
    public String postNewMembers(@Valid MembersFormDto membersFormDto, Model model, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "members/membersForm";
        }
        try {
            //주소 처리
            Address address = new Address(membersFormDto.getCity()
                    , membersFormDto.getStreet(), membersFormDto.getZipcode());
            membersFormDto.setAddress(address);
            //주소 처리
            Members members = Members.createMembers(membersFormDto, passwordEncoder);
            membersService.saveMembers(members);
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "members/membersForm";
        }
        return "redirect:/";
    }

    @GetMapping("/login")
    public String getLoginMember() {
        return "members/membersLoginForm";
    }

    @GetMapping("/login/error")
    public String loginError(Model model) {
        model.addAttribute("loginErrorMessage", "아이디 또는 비밀번호를 확인해 주세요.");
        return "members/membersLoginForm";
    }

    @GetMapping("/update")
    public String getUpdate(Model model, Principal principal) {
        model.addAttribute("membersUpdateDto", new MembersUpdateDto());
        return "members/updateForm";
    }

    @PostMapping("/update")
    public String postUpdate(Authentication authentication, @Valid MembersUpdateDto membersUpdateDto, Model model,
                             BindingResult bindingResult, Principal principal) {
        String email = principal.getName();
        if (bindingResult.hasErrors()) {
            return "members/updateForm";
        }
        try {
            membersService.updateMembers(membersUpdateDto, email);
//update를 안해도 더티체킹을 통해 자동으로 적용됨.
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "members/updateForm";
        }

        return "redirect:/";
    }

}
