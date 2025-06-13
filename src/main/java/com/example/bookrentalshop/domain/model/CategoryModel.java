package com.example.bookrentalshop.domain.model;

import com.example.bookrentalshop.domain.entity.CategoryEntity;
import com.example.bookrentalshop.repository.CategoryRepository;
import com.google.common.collect.Maps;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Getter
@RequiredArgsConstructor
public class CategoryModel {

    private final Map<Integer, CategoryEntity> idMap = Maps.newHashMap();
    private final Map<String, CategoryEntity> nameMap = Maps.newHashMap();

    private final CategoryRepository categoryRepository;

    @PostConstruct
    public void init() {
        categoryRepository.findAll().forEach(category -> {
            idMap.put(category.getId(), category);
            nameMap.put(category.getName(), category);
        });
    }
}
