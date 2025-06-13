package com.example.bookrentalshop.repository;

import com.example.bookrentalshop.domain.entity.RentalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RentalRepository extends JpaRepository<RentalEntity, Long>, RentalRepositoryCustom {

    Optional<RentalEntity> findByIdAndUserId(Long id, Long userId);
}
