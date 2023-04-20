package com.example.springbootshop.entity;

import com.example.springbootshop.constant.Role;
import com.example.springbootshop.dto.MembersFormDto;
import com.example.springbootshop.dto.MembersUpdateDto;
import com.example.springbootshop.vo.Address;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "members")
public class Members extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "members")
    private List<Order> orderList = new ArrayList<>();

    public static Members createMembers(MembersFormDto membersFormDto, PasswordEncoder passwordEncoder) {
        Members members = new Members();
        members.setName(membersFormDto.getName());
        members.setEmail(membersFormDto.getEmail());
        members.setAddress(membersFormDto.getAddress());
        String password = passwordEncoder.encode(membersFormDto.getPassword());
        members.setPassword(password);
        members.setRole(Role.SELLER);
        return members;
    }

    public void update(String name, String password) {
        this.name = name;
        this.password = password;
    }

}
