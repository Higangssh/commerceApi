package com.the.commerce.model.entity;

import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@ToString
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

    @CreatedDate
    @Column(name = "create_at")
    private LocalDateTime createAt;

    @LastModifiedDate
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @PrePersist
    public void formattingBeforeCreateDate() {
        String customLocalDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.createAt = LocalDateTime.parse(customLocalDateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.modifiedAt = LocalDateTime.parse(customLocalDateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    @PreUpdate
    public void formattingBeforeModifiedDate() {
        String customLocalDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.modifiedAt = LocalDateTime.parse(customLocalDateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }


}
