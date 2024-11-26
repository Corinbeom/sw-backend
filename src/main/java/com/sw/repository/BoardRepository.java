package com.sw.repository;

import com.sw.model.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findByLeague(String league); // 리그별 게시글 조회
}

