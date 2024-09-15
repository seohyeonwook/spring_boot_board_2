package com.git.board2.entity;

import com.git.board2.dto.BoardDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

// DB의 테이블 역할 하는 클래스
@Entity
@Getter
@Setter
@Table(name ="board_table") // 테이블 이름 정하기
public class BoardEntity extends BaseEntity { // BoardEntity 가 BaseEntity 를 상속 받는다

    @Id // pk 컬럼 지정. 필수
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    private Long id;

    @Column(length = 20, nullable = false) // 크기 20, not null
    private String boardWriter;

    @Column // 디폴트 값  = 크기 255, null 가능
    private String boardPass;
    private String boardTitle;
    @Column(length = 500)
    private String boardContents;
    private int boardHits;
    // 시간 관련된건 BaseEntity에서

    public static BoardEntity toSaveEntity(BoardDTO boardDTO) {
        BoardEntity boardEntity = new BoardEntity();
        boardEntity.setBoardWriter(boardDTO.getBoardWriter());
        boardEntity.setBoardPass(boardDTO.getBoardPass());
        boardEntity.setBoardTitle(boardDTO.getBoardTitle());
        boardEntity.setBoardContents(boardDTO.getBoardContents());
        boardEntity.setBoardHits(0);
        // 조회수는 기본 0이니까 이걸 결국 new한 boardEntity 에 담아주는작업이기때문에
        return boardEntity;
    }

    public static BoardEntity toUpdateEntity(BoardDTO boardDTO) {
        BoardEntity boardEntity = new BoardEntity();
        boardEntity.setId(boardDTO.getId()); // 이 아이디 부분이있어야 update쿼리 전달가능
        boardEntity.setBoardWriter(boardDTO.getBoardWriter());
        boardEntity.setBoardPass(boardDTO.getBoardPass());
        boardEntity.setBoardTitle(boardDTO.getBoardTitle());
        boardEntity.setBoardContents(boardDTO.getBoardContents());
        boardEntity.setBoardHits(boardDTO.getBoardHits());
        return boardEntity;
    }
}
