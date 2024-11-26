package com.sw.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    private String nickName; // 작성자를 닉네임으로 저장

    @Column(nullable = true)
    private String imageUrl; // 댓글 이미지 URL

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board; // 기존의 `post`를 `board`로 변경

    @Column(nullable = false)
    private int likeCount = 0; // 댓글 좋아요

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Comment parent; // 대댓글

    private LocalDateTime createdTime = LocalDateTime.now();
}
