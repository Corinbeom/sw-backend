package com.sw.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = true, length = 16)
    private String userId;

    @Column(nullable = true, length = 16)
    private String password;

    @Column(unique = true, length = 10)
    private String nickName;

    @Column(unique = true)
    private String userEmail;

    private String tier;

    private String profileImage;

    private Date createdTime;

    private Date updatedTime;

    private String verificationToken;

    private String role;

    private boolean isVerified = false;


}
