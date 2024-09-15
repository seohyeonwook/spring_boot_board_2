package com.git.board2.repository;

import com.git.board2.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
    // 레파지토리는 기본적으로 엔티티 객체만 받아준다
    // JpaRepository 상속받으면 findAll이나 save같은건 따로 안만들어도 내장 되어있는거사용가능

    // 조회수 증가 - update board_table set board_hits=board_hits +1 where id=?
            //      현재 가지고있는 조회수에서 하나 증가 (where id=?해당 게시글만 찾기)

    @Modifying // update나 delete 같은건 필수로 붙여햐함
    @Query(value = "update BoardEntity b set b.boardHits = b.boardHits +1 where b. id=:id")
    // entity 기준으로 만든 쿼리문  entity 는 약어를 쓰는게 규칙 BoardEntity 를 b라는 약어로규정
    // 실제 데이터베이스에 컬럼이름이아니라 entity 의 변수명 과 동일하게
    // 맨 뒤 id부분이 계속 바뀌는 부분 = 밑에 Param에 id 와 :id가 같다
    void updateHits(@Param("id") Long id);
}
