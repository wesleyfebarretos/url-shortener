package com.spring.app.urlshorter.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Table(name = "\"user\"")
@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column
    String firstName;

    @Column
    String lastName;

    @Column
    String userName;

    @Column
    String password;

    @Column
    @CreationTimestamp
    ZonedDateTime createdAt;

    @Column
    @UpdateTimestamp
    ZonedDateTime updatedAt;

    @OneToMany(mappedBy = "user")
    List<UrlEntity> urls;
}