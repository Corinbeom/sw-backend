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

    @Column(unique = true)
    private String userEmail;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, length = 10)
    private String nickName;

    private Date birthDate;

    private String tier;

    private String profileImage;

    private Date createdTime;

    private Date updatedTime;

    private String verificationToken;

    private String role;

    private boolean isVerified = false;


}
