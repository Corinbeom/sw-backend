package com.sw.controller;

import com.sw.config.JwtUtils;
import com.sw.model.Board;
import com.sw.model.BoardImage;
import com.sw.model.dto.BoardDetailResponse;
import com.sw.service.BoardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/boards")
public class BoardController {

    private final Logger logger = LoggerFactory.getLogger(BoardController.class);

    private final BoardService boardService;

    @Autowired
    private JwtUtils jwtUtils;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @PostMapping
    public ResponseEntity<?> createBoard(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("league") String league,
            @RequestParam(value = "images", required = false) List<MultipartFile> images,
            @RequestHeader("Authorization") String token
    ) throws IOException {
        // JWT 검증 및 사용자 정보 추출
        if (!jwtUtils.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰입니다.");
        }

        String nickName = jwtUtils.extractNickName(token);
        if (nickName == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("사용자 인증 실패");
        }

        // 게시글 생성
        Board board = new Board();
        board.setTitle(title);
        board.setContent(content);
        board.setLeague(league);
        board.setNickName(nickName); // 닉네임으로 작성자 설정

        Board savedBoard = boardService.createBoard(board,images,nickName,league);
        return ResponseEntity.ok(savedBoard);
    }

    @GetMapping
    public ResponseEntity<?> getBoardsByLeague(@RequestParam String league) {
        List<Board> boards = boardService.getBoardsByLeague(league);
        return ResponseEntity.ok(boards);
    }

    /**
     * 게시글 상세 조회
     */
    @GetMapping("/{boardId}")
    public ResponseEntity<?> getBoardById(@PathVariable Long boardId) {
        Board board = boardService.getBoardById(boardId);
        if (board == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("게시글을 찾을 수 없습니다.");
        }

        // Board와 관련된 BoardImage 데이터 포함
        List<String> fileUrls = board.getImages().stream()
                .map(BoardImage::getFileUrl)
                .toList();

        logger.info("게시글 ID: {}, 제목: {}", board.getId(), board.getTitle());
        board.getImages().forEach(image -> logger.info("이미지 URL: {}", image.getFileUrl()));


        return ResponseEntity.ok(new BoardDetailResponse(board, fileUrls));
    }

    @PostMapping("/{boardId}/mom")
    public ResponseEntity<?> addMom(@PathVariable Long boardId) {
        boardService.addMom(boardId);
        return ResponseEntity.ok("MOM updated");
    }

    @PostMapping("/{boardId}/worst")
    public ResponseEntity<?> addWorst(@PathVariable Long boardId) {
        boardService.addWorst(boardId);
        return ResponseEntity.ok("Worst updated");
    }
}

