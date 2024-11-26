package com.sw.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "board_image")
public class BoardImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "board_id")
    @JsonBackReference
    private Board board;

    @Column(nullable = false)
    private String fileType; // 파일 타입 (image, video)

    @Column(nullable = false)
    private String fileUrl; // 파일 경로(URL 또는 파일 시스템 경로)
}
