package com.example.bookrentalshop.repository;

import com.example.bookrentalshop.domain.entity.BookEntity;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookRepositoryCustom {

    Page<BookEntity> findAll(Predicate predicate, Pageable pageable);
}
