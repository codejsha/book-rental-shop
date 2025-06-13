package com.example.bookrentalshop.repository;

import com.example.bookrentalshop.domain.entity.QRentalEntity;
import com.example.bookrentalshop.domain.entity.RentalEntity;
import com.google.common.collect.Lists;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class RentalRepositoryCustomImpl implements RentalRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<RentalEntity> findAll(Predicate predicate, Pageable pageable) {
        QRentalEntity rental = QRentalEntity.rentalEntity;

        PathBuilder<RentalEntity> pathBuilder = new PathBuilder<>(rental.getType(), rental.getMetadata());
        List<OrderSpecifier<?>> orderSpecifiers = Lists.newArrayList();
        pageable.getSort().forEach(order -> {
            var direction = order.isAscending() ? Order.ASC : Order.DESC;
            var target = pathBuilder.get(order.getProperty());
            orderSpecifiers.add(new OrderSpecifier(direction, target));
        });

        List<RentalEntity> fetch = jpaQueryFactory
                .selectFrom(rental)
                .where(predicate)
                .orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(rental.count())
                .from(rental)
                .where(predicate);

        return PageableExecutionUtils.getPage(fetch, pageable, countQuery::fetchOne);
    }
}
