package com.example.springbootshop.repository;

import com.example.springbootshop.dto.ItemSearchDto;
import com.example.springbootshop.entity.Item;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;

import static com.example.springbootshop.entity.QItem.item;

public class OrderRepositoryCustomImpl implements OrderRepositoryCustom {

    private JPAQueryFactory queryFactory;

    public OrderRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    //order_items에서 created_by를 확인, 판매자의 상품 정보 가져와야 하고
    //
    //해당 정보를 바탕으로 order_id를 통해 order 가져와야 한다

    private BooleanExpression searchByCreatedBy(String createdBy) {
        return createdBy == null ? null : item.createdBy.eq(createdBy);
    }

    @Override
    public Page<Item> getOrders(ItemSearchDto itemSearchDto, Pageable pageable) {


        return null;
    }


}
