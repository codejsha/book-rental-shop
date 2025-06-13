package com.example.bookrentalshop.domain.entity;

import com.example.bookrentalshop.controller.dto.BookCreateRequest;
import com.example.bookrentalshop.controller.dto.BookGetResponse;
import com.example.bookrentalshop.controller.dto.BookUpdateResponse;
import com.example.bookrentalshop.domain.command.BookUpdateCommand;
import com.example.bookrentalshop.domain.constant.BookStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "book")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class BookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String author;

    private String title;

    @Enumerated(EnumType.STRING)
    private BookStatus status;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity category;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Version
    private Long version;

    public void update(BookUpdateCommand cmd) {
        if (cmd.getAuthor() != null) this.author = cmd.getAuthor();
        if (cmd.getTitle() != null) this.title = cmd.getTitle();
        if (cmd.getStatus() != null) this.status = cmd.getStatus();
        if (cmd.getCategory() != null) this.category = cmd.getCategory();
    }

    public boolean isStatusAvailable() {
        return this.status == BookStatus.AVAILABLE;
    }

    public boolean isStatusCheckedOut() {
        return this.status == BookStatus.CHECKED_OUT;
    }

    public static BookEntity newInstance(BookCreateRequest req, CategoryEntity category) {
        return BookEntity.builder()
                .author(req.getAuthor())
                .title(req.getTitle())
                .status(BookStatus.STATUS_MAP.get(req.getStatus()))
                .category(category)
                .build();
    }

    public BookGetResponse toBookGetResponse() {
        return BookGetResponse.builder()
                .id(this.id)
                .author(this.author)
                .title(this.title)
                .status(this.status.toString())
                .category(this.category.getName())
                .build();
    }

    public BookUpdateResponse toUpdateResponse() {
        return BookUpdateResponse.builder()
                .id(this.id)
                .author(this.author)
                .title(this.title)
                .status(this.status.toString())
                .category(this.category.getName())
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof BookEntity book)) return false;
        return Objects.equals(id, book.id)
               && Objects.equals(author, book.author)
               && Objects.equals(title, book.title)
               && status == book.status
               && Objects.equals(category, book.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, author, title, status, category);
    }
}
