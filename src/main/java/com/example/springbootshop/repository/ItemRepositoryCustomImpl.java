package com.example.springbootshop.repository;

import com.example.springbootshop.constant.Category;
import com.example.springbootshop.constant.ItemSellStatus;
import com.example.springbootshop.dto.ItemSearchDto;
import com.example.springbootshop.dto.MainItemDto;
import com.example.springbootshop.dto.QMainItemDto;
import com.example.springbootshop.entity.Item;
import com.example.springbootshop.entity.QItem;
import com.example.springbootshop.entity.QItemImg;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

import static com.example.springbootshop.entity.QItem.item;

public class ItemRepositoryCustomImpl implements ItemRepositoryCustom {

    private JPAQueryFactory queryFactory;//querydsl을 사용하기 위함

    public ItemRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }//JpaQueryFactory로 em을 래핑한다. em을 직접 다루지 않아도 된다. 코드 가독성이 좋아지고 멀티스레드 환경에서 안전하게 사용 가능.

    private BooleanExpression searchSellStatusEq(ItemSellStatus searchSellStatus) {
        return searchSellStatus == null ? null : item.itemSellStatus.eq(searchSellStatus);
        //상품 판매 상태 조건이 전체(null)일 경우는 null 리턴, 판매중 or 품절이면 해당 조건의 상품 조회
    }

    private BooleanExpression searchCategoryStatusEq(Category category) {
        return category == null ? null : item.category.eq(category);
    }

    private BooleanExpression regDtsAfter(String searchDateType) {
        LocalDateTime dateTime = LocalDateTime.now();

        if (StringUtils.equals("all", searchDateType) || searchDateType == null) {
            return null;
        } else if (StringUtils.equals("1d", searchDateType)) {
            dateTime = dateTime.minusDays(1);
        } else if (StringUtils.equals("1w", searchDateType)) {
            dateTime = dateTime.minusWeeks(1);
        } else if (StringUtils.equals("1m", searchDateType)) {
            dateTime = dateTime.minusMonths(1);
        } else if (StringUtils.equals("6m", searchDateType)) {
            dateTime = dateTime.minusMonths(6);
        }

        return item.createdAt.after(dateTime);
    }

    private BooleanExpression searchByLike(String searchBy, String searchQuery) {

        if (StringUtils.equals("itemName", searchBy)) {
            return item.itemName.like("%" + searchQuery + "%");
        } else if (StringUtils.equals("createdBy", searchBy)) {
            return item.createdBy.like("%" + searchQuery + "%");
        }

        return null;
    }

    private BooleanExpression createdByLike(String createdBy) {
        return item.createdBy.like(createdBy);
    }

    @Override
    public Page<Item> getSellerItemPage(ItemSearchDto itemSearchDto, Pageable pageable, String createdBy) {

        List<Item> content = queryFactory
                .selectFrom(item)
                .where(regDtsAfter(itemSearchDto.getSearchDateType()),
                        searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
                        searchByLike(itemSearchDto.getSearchBy(),
                                itemSearchDto.getSearchQuery()),
                        searchCategoryStatusEq(itemSearchDto.getSearchCategory()),
                        createdByLike(createdBy)//여기 추가
                )//수정해야댐
                .orderBy(QItem.item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.select(Wildcard.count).from(QItem.item)//wildcard.count는 count(*)임
                .where(regDtsAfter(itemSearchDto.getSearchDateType())
                        , searchSellStatusEq(itemSearchDto.getSearchSellStatus())
                        , searchByLike(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery())
                        , searchCategoryStatusEq(itemSearchDto.getSearchCategory()))
                .fetchOne();
        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression itemNameLike(String searchQuery) {
        return StringUtils.isEmpty(searchQuery) ? null : item.itemName.like("%" + searchQuery + "%");
    }

    @Override
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;

        List<MainItemDto> content = queryFactory
                .select(
                        new QMainItemDto(
                                item.id,
                                item.itemName,
                                item.itemDetail,
                                itemImg.imgUrl,
                                item.price)
                )
                .from(itemImg)
                .join(itemImg.item, item)
                .where(itemImg.repimgYn.eq("Y"))
                .where(itemNameLike(itemSearchDto.getSearchQuery()))
                .orderBy(item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(Wildcard.count)
                .from(itemImg)
                .join(itemImg.item, item)
                .where(itemImg.repimgYn.eq("Y"))
                .where(itemNameLike(itemSearchDto.getSearchQuery()))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

}
