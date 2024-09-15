package com.git.board2.controller;

import com.git.board2.dto.BoardDTO;
import com.git.board2.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/save")
    public String saveForm() {
        return "/save";
    }
    @PostMapping("/save")
    public String save(@ModelAttribute BoardDTO boardDTO) {
        System.out.println("boardDTO = " + boardDTO);// 확인용
        boardService.save(boardDTO);
        return "index";
    }

    //------------------------------------------------------------------------

    @GetMapping("/")
    public String findAll(Model model) {
        // DB에서 전체 게시글 데이터를 가져와서 list.html에 보여준다
        List<BoardDTO> boardDTOList = boardService.findAll();
        model.addAttribute("boardList",boardDTOList);
        return "List";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable Long id , Model model,
                           @PageableDefault(page = 1 ) Pageable pageable) {
        // Pageable - 페이지 번호, 페이지 크기, 정렬 정보를 가지고있음
        // 두가지 생각해야함 -  2가지 작업 - 2번의 호출
        // 해당 게시글의 조회수를 하나 올리고
        // 게시글 데이터를 가져와서 deatil.html에 출력해야함
        boardService.updateHists(id);
        BoardDTO boardDTO = boardService.findById(id);
        model.addAttribute("board",boardDTO);
        model.addAttribute("page", pageable.getPageNumber());
                                                // 현재 페이지 번호를 반환
        // 현재 페이지 번호를 뷰로 전달.
        return "detail";
        // 나중에 글목록에서 해당글 누르고 뒤로가기 눌러도 2페이면 2페이지 유지되게끔

    }

    //------------------------------------------------------------------------

    // 수정 -
    //      상세화면에서 수정 버튼 클릭
    //      서버에서 해당 게시글의 정보를 가지고 수정 화면 출력
    //      제목, 내용 수정 입력 받아서 서버로 요청
    //      수정 처리
    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable Long id, Model model){
        BoardDTO boardDTO = boardService.findById(id);
        // 게시글의 정보 불러오기
        model.addAttribute("boardUpdate", boardDTO);
        // 정보를 가지고 update.html로
        return "update";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute BoardDTO boardDTO, Model model) {
        BoardDTO board = boardService.update(boardDTO);
        // model.addAttribute - 정보를 가지고 return으로 간다고 생각하자 일단
        model.addAttribute("board", board);
        return "detail";
    }

    //------------------------------------------------------------------------

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        boardService.delete(id);
        return "redirect:/board/";
    }

    //------------------------------------------------------------------------
    // 페이징 처리 - 난이도 어려움
    // /board/paging? page =1
    @GetMapping("/paging")
    public String paging(@PageableDefault( page = 1 ) Pageable pageable, Model model) {
        // @PageableDefault( page = 1 ) 기본페이지 를 1번으로 하겠다
        // 처리된 페이지를 가지고 화면으로 넘어가야하기때문에 model

        Page<BoardDTO> boardList = boardService.paging(pageable); //pageable 페이지 값을 넘겨준다

        // page 갯수 20개 가 있다고 가정 - 현재 사용자가 3페이지를 보고있으면 - 페이지마다 다르지만
        // 페이징의 보여주는방식이 각자 다르다 eg) 1 2 3 4 5에서 내가 3페이지면 3만색이다르다
        // 내가 만드는건 보여지는 페이지 갯수 3개만 보여준다
        // eg) 내가 3페이지에 있으면 1 2 3 내가 7페이지에 있으면 7 8 9
        int blockLimit = 3;
        int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1; // 1 4 7 10 ~~
        // 하지만 총 페이지 갯수가 8페이지면 9페이지를 보여주지말고 전체페이지(8) 값으로
        int endPage = ((startPage + blockLimit - 1) < boardList.getTotalPages()) ? startPage + blockLimit - 1 : boardList.getTotalPages();

        model.addAttribute("boardList", boardList);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "paging";

    }
}
