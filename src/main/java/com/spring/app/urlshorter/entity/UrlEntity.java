package com.spring.app.urlshorter.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;

@Entity
@Table(name = "url")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UrlEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column
    String shortCode;

    @Column
    String originalAddress;

    @Column
    int accessQty;

    @Column
    ZonedDateTime expirationAt;

    @Column
    @CreationTimestamp
    ZonedDateTime createdAt;

    @Column
    @UpdateTimestamp
    ZonedDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    UserEntity user;
}


