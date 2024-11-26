package com.sw.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    private String nickName; // 작성자를 닉네임으로 저장

    @Column(nullable = false)
    private String league; // 게시판 종류: EPL, Laliga, etc.

    @Column(nullable = false)
    private int momCount = 0; // 좋아요(MOM)

    @Column(nullable = false)
    private int worstCount = 0; // 싫어요(Worst)

    @Column(nullable = false)
    private int viewCount = 0;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardImage> images = new ArrayList<>(); // 첨부된 이미지와 영상

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    private LocalDateTime createdTime = LocalDateTime.now();
}

