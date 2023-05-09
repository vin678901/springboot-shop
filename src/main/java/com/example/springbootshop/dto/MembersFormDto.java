package com.example.springbootshop.dto;

import com.example.springbootshop.vo.Address;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class MembersFormDto {

    @NotEmpty(message = "이메일을 입력해 주세요.")
    @Email(message = "잘못된 이메일 형식입니다.")
    private String email;

    @NotBlank(message = "이름을 입력해 주세요.")
    private String name;

    @NotEmpty(message = "비밀번호를 입력해 주세요")
    @Length(min = 8, max = 16, message = "비밀번호는 8자 이상, 16자 이하로 입력해주세요")
    private String password;


    private Address address;
    //Address에는 city, street, zipcode가 있다.
    private String city;

    private String street;

    private String zipcode;


}
