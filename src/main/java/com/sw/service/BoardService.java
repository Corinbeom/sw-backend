package com.sw.service;

import com.sw.model.Board;
import com.sw.model.BoardImage;
import com.sw.model.User;
import com.sw.repository.BoardImageRepository;
import com.sw.repository.BoardRepository;
import com.sw.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BoardService {

    private static final Logger logger = LoggerFactory.getLogger(BoardService.class);

    @Autowired
    private  BoardRepository boardRepository;
    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private  BoardImageRepository boardImageRepository;
    @Autowired
    private BoardImageService boardImageService;


    public Board createBoard(Board board, List<MultipartFile> images, String nickName, String league) throws IOException {
        User user = userRepository.findByNickName(nickName)
                .orElseThrow(() -> new RuntimeException("User not found"));

        board.setUser(user);
        board.setLeague(league);
        board.setCreatedTime(LocalDateTime.now());
        Board savedBoard = boardRepository.save(board);

        List<BoardImage> boardImages = saveImages(savedBoard,images);
        boardImageRepository.saveAll(boardImages);

        return savedBoard;
    }

    private List<BoardImage> saveImages(Board board, List<MultipartFile> images) {
        if (images == null || images.isEmpty()) {
            return List.of(); // 이미지가 없으면 빈 리스트 반환
        }

        return images.stream()
                .map(image -> {
                    try {
                        return saveImage(board, image);
                    } catch (IOException e) {
                        logger.error("이미지 저장 실패: {}", image.getOriginalFilename(), e);
                        throw new RuntimeException("이미지 저장 중 오류가 발생했습니다.");
                    }
                })
                .collect(Collectors.toList());
    }

    // 단일 이미지 저장
    private BoardImage saveImage(Board board, MultipartFile image) throws IOException {
        // 1. 파일 이름 처리
        String originalFilename = image.getOriginalFilename();
        String fileType = Objects.requireNonNull(image.getContentType()).startsWith("image/") ? "image" : "video";
        String uniqueFileName = UUID.randomUUID() + "_" + originalFilename; // 유니크한 파일 이름 생성

        // 2. 저장 경로 생성
        Path uploadDir = Paths.get("uploads/boardImage");
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir); // 디렉토리가 없으면 생성
        }

        // 3. 파일 저장
        Path filePath = uploadDir.resolve(uniqueFileName);
        Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // 4. BoardImage 엔티티 생성 및 반환
        BoardImage boardImage = new BoardImage();
        boardImage.setFileType(fileType); // 파일 타입 설정
        boardImage.setFileUrl(filePath.toString()); // 저장된 파일 경로
        boardImage.setBoard(board); // 연관된 Board 설정
        return boardImage;
    }


    public List<Board> getBoardsByLeague(String league) {
        return boardRepository.findByLeague(league);
    }

    public void addMom(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("Board not found"));
        board.setMomCount(board.getMomCount() + 1);
        boardRepository.save(board);
    }

    public void addWorst(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("Board not found"));
        board.setWorstCount(board.getWorstCount() + 1);
        boardRepository.save(board);
    }

    public Board getBoardById(Long boardId) {
        return boardRepository.findById(boardId).orElse(null);
    }
}

