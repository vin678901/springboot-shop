package com.example.springbootshop.dto;


import com.example.springbootshop.vo.Address;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class MembersUpdateDto {

    @NotBlank(message = "수정할 이름을 입력해 주세요.")
    private String name;

    @NotEmpty(message = "수정할 비밀번호를 입력해 주세요")
    @Length(min = 8, max = 16, message = "비밀번호는 8자 이상, 16자 이하로 입력해주세요")
    private String password;
    

}
