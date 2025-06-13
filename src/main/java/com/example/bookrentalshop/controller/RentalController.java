package com.example.bookrentalshop.controller;

import com.example.bookrentalshop.controller.dto.*;
import com.example.bookrentalshop.domain.entity.RentalEntity;
import com.example.bookrentalshop.domain.service.RentalService;
import com.example.bookrentalshop.support.security.JwtAuthenticationPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/rentals")
@RequiredArgsConstructor
public class RentalController {

    private final RentalService rentalService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_RENTAL_MANAGE', 'ROLE_ADMIN')")
    public ResponseEntity<Page<RentalGetResponse>> getAllRentals(
            @ModelAttribute RentalCondition condition,
            @PageableDefault(size = 10, page = 0, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        var result = rentalService.getAllRentals(condition, pageable);
        var response = result.map(RentalEntity::toRentalGetResponse);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_RENTAL_REQUEST', 'ROLE_RENTAL_MANAGE', 'ROLE_ADMIN')")
    public ResponseEntity<RentalCheckOutResponse> checkOutRental(
            @Valid @RequestBody RentalCheckOutRequest req,
            @AuthenticationPrincipal JwtAuthenticationPrincipal principal) {
        var rental = rentalService.createCheckOut(req, principal);
        return ResponseEntity.ok(rental.toRentalCheckOutResponse());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_RENTAL_MANAGE', 'ROLE_ADMIN')")
    public ResponseEntity<RentalGetResponse> getRental(@PathVariable Long id) {
        var rental = rentalService.getRental(id);
        return ResponseEntity.ok(rental.toRentalGetResponse());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_RENTAL_REQUEST','ROLE_RENTAL_MANAGE', 'ROLE_ADMIN')")
    public ResponseEntity<RentalReturnResponse> returnRental(@PathVariable Long id,
                                                             @AuthenticationPrincipal JwtAuthenticationPrincipal principal) {
        var rental = rentalService.createReturn(id, principal);
        return ResponseEntity.ok(rental.toRentalReturnResponse());
    }

    @GetMapping("/my-rentals")
    @PreAuthorize("hasAnyAuthority('ROLE_RENTAL_READ_OWNER', 'ROLE_RENTAL_MANAGE', 'ROLE_ADMIN')")
    public ResponseEntity<Page<RentalGetResponse>> getMyRentals(
            @ModelAttribute RentalCondition condition,
            @PageableDefault(size = 10, page = 0, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal JwtAuthenticationPrincipal principal) {
        var result = rentalService.getAllRentals(condition, pageable, principal);
        var response = result.map(RentalEntity::toRentalGetResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my-rentals/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_RENTAL_READ_OWNER', 'ROLE_RENTAL_MANAGE', 'ROLE_ADMIN')")
    public ResponseEntity<RentalGetResponse> getMyRental(@PathVariable Long id,
                                                         @AuthenticationPrincipal JwtAuthenticationPrincipal principal) {
        var rental = rentalService.getRental(id, principal);
        return ResponseEntity.ok(rental.toRentalGetResponse());
    }
}
