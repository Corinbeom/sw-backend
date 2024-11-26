package com.sw.model.dto;

import com.sw.model.Board;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
public class BoardDetailResponse {
    private Long id;
    private String league;
    private String title;
    private String content;
    private String nickName;
    private LocalDateTime createdTime;
    private List<String> fileUrls; // 첨부 파일 URL 목록

    public BoardDetailResponse(Board board, List<String> fileUrls) {
        this.id = board.getId();
        this.league = board.getLeague();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.nickName = board.getNickName();
        this.createdTime = board.getCreatedTime();
        this.fileUrls = fileUrls;
    }
}
