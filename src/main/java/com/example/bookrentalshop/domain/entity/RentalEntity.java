package com.example.bookrentalshop.domain.entity;

import com.example.bookrentalshop.controller.dto.RentalCheckOutResponse;
import com.example.bookrentalshop.controller.dto.RentalGetResponse;
import com.example.bookrentalshop.controller.dto.RentalReturnResponse;
import com.example.bookrentalshop.domain.command.ReturnBookCommand;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "rental")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class RentalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private BookEntity book;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    private LocalDateTime checkOutDate;

    private LocalDateTime returnDate;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Version
    private Long version;

    public boolean isMyRental(UserEntity user) {
        return this.user.getId().equals(user.getId());
    }

    public void returnBook(ReturnBookCommand cmd) {
        this.returnDate = cmd.getReturnDate();
    }

    public static RentalEntity newInstance(BookEntity book, UserEntity user) {
        return RentalEntity.builder()
                .book(book)
                .user(user)
                .checkOutDate(LocalDateTime.now())
                .build();
    }

    public RentalGetResponse toRentalGetResponse() {
        return RentalGetResponse.builder()
                .id(this.id)
                .bookId(this.book.getId())
                .bookTitle(this.book.getTitle())
                .userId(this.user.getId())
                .userName(this.user.getName())
                .checkOutDate(this.checkOutDate)
                .returnDate(this.returnDate)
                .build();
    }

    public RentalCheckOutResponse toRentalCheckOutResponse() {
        return RentalCheckOutResponse.builder()
                .bookId(this.book.getId())
                .userId(this.user.getId())
                .checkOutDate(this.checkOutDate)
                .build();
    }

    public RentalReturnResponse toRentalReturnResponse() {
        return RentalReturnResponse.builder()
                .bookId(this.book.getId())
                .userId(this.user.getId())
                .checkOutDate(this.checkOutDate)
                .returnDate(this.returnDate)
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof RentalEntity rental)) return false;
        return Objects.equals(id, rental.id)
               && Objects.equals(book, rental.book)
               && Objects.equals(user, rental.user)
               && Objects.equals(checkOutDate, rental.checkOutDate)
               && Objects.equals(returnDate, rental.returnDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, book, user, checkOutDate, returnDate);
    }
}
