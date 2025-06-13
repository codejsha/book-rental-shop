package com.example.bookrentalshop.repository;

import com.example.bookrentalshop.domain.entity.RentalEntity;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RentalRepositoryCustom {

    Page<RentalEntity> findAll(Predicate predicate, Pageable pageable);
}
