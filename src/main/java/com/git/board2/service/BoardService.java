package com.git.board2.service;

import com.git.board2.dto.BoardDTO;
import com.git.board2.entity.BoardEntity;
import com.git.board2.repository.BoardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
// service클래스에서는 dto -> entity (Entity클래스에서)
// / entity -> dto(DTO클래스에서)로 바꾸는 작업 많이함
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    public void save(BoardDTO boardDTO) {
        // 레파지토리는 기본적으로 엔티티 객체만 받아준다

        BoardEntity boardEntity  = BoardEntity.toSaveEntity(boardDTO);

        boardRepository.save(boardEntity);
    }

    public List<BoardDTO> findAll() {
        List<BoardEntity> boardEntityList = boardRepository.findAll();
        // 항상 기억하자 레파지토리는 무조건 엔티티와 같이있다
        //entity -> dto
        List<BoardDTO> boardDTOList = new ArrayList<>();
        // 여기다 아래 변환한 리스트 담아준다

        for( BoardEntity boardEntity : boardEntityList) {
            boardDTOList.add(BoardDTO.toBoardDTO(boardEntity));
        }
        return boardDTOList;
    }

    @Transactional // JPA에서 제공하는게아니라 우리가 만들면 붙여야함
    public void updateHists(Long id) {
        // 이건 JPA에 없는 메소드라 repository에 정의해줘야함
        boardRepository.updateHits(id);
    }

    public BoardDTO findById(Long id) {
        Optional<BoardEntity> byId = boardRepository.findById(id);
        if(byId.isPresent()) { // id가 있으면
            BoardEntity boardEntity = byId.get(); // optional에서 꺼내서
            BoardDTO boardDTO = BoardDTO.toBoardDTO(boardEntity);// dto로
            return boardDTO;
        } else {
            return null;
        }
    }

    public BoardDTO update(BoardDTO boardDTO) {
        // JPA에 save는 insert와 update 두가지 기능이있다
        // 구분법은 update는 id값이 있다
        BoardEntity boardEntity  = BoardEntity.toUpdateEntity(boardDTO);
        boardRepository.save(boardEntity);
        return findById(boardDTO.getId());
        // 업데이트된 게시글을 다시 찾아서 반환
    }

    public void delete(Long id) {
        boardRepository.deleteById(id);
    }



    public Page<BoardDTO> paging(Pageable pageable) {
        int page = pageable.getPageNumber() - 1; //  몇페이지 보고싶은지
        // page 위치에 있는 값은 0부터 시작 그래서 - 1 ;
        int pageLimit = 3; // 한 페이지에 보여줄 글 갯수
        // 한 페이지당 3개씩 글을 보여주고 정렬 기준은 id기준으로 내림차순 정렬

        Page<BoardEntity> boardEntities =
                boardRepository.findAll(PageRequest.of(page,pageLimit, Sort.by(Sort.Direction.DESC,"id")));
                // Sort.by(Sort.Direction.DESC,"id" - 어떻게 정렬 해서 가져올거냐 / Direction- 내림차순  /"id" - entity 기준

        System.out.println("boardEntities.getContent() = " + boardEntities.getContent()); // 요청 페이지에 해당하는 글
        System.out.println("boardEntities.getTotalElements() = " + boardEntities.getTotalElements()); // 전체 글갯수
        System.out.println("boardEntities.getNumber() = " + boardEntities.getNumber()); // DB로 요청한 페이지 번호
        System.out.println("boardEntities.getTotalPages() = " + boardEntities.getTotalPages()); // 전체 페이지 갯수
        System.out.println("boardEntities.getSize() = " + boardEntities.getSize()); // 한 페이지에 보여지는 글 갯수
        System.out.println("boardEntities.hasPrevious() = " + boardEntities.hasPrevious()); // 이전 페이지 존재 여부
        System.out.println("boardEntities.isFirst() = " + boardEntities.isFirst()); // 첫 페이지 여부
        System.out.println("boardEntities.isLast() = " + boardEntities.isLast()); // 마지막 페이지 여부

        // 목록: id, writer, title, title, hits, createTime - 이 자료만 보여주면 된다 map 이라는 메서드 이해하자 중요
        Page<BoardDTO> boardDTOS = boardEntities.map( board -> new BoardDTO(
                board.getId(),
                board.getBoardWriter(),
                board.getBoardTitle(),
                board.getBoardHits(),
                board.getCreatedTime())); // 우리 초기에쓰던 new 생성자에 매개변수 담는 방법
        // board - entity 객체 매개변수라고 생각
        // 위에 findAll 처럼 리스트 형태로 entity -> dto로 가져가면 좋은데 이건 List 가아니라 Page라서 다른방법
        // board에서 변수를 하나씩 꺼내서 DTO 객체로 옮겨 담는 작업 그러면서 위에 프린터문 다 가져간다
        return boardDTOS;
    }
}
