package com.example.springbootshop.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Min;

@Entity
@Table(name = "cart")
@Getter
@Setter
@ToString
public class Cart extends BaseEntity {

    @Id
    @Column(name = "cart_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Members members;//한명의 유저는 하나의 장바구니를 가진다


    public static Cart createCart(Members members) {
        Cart cart = new Cart();
        cart.setMembers(members);
        return cart;
    }
}
