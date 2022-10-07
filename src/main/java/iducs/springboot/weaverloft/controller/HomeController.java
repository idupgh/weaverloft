package iducs.springboot.weaverloft.controller;

import iducs.springboot.weaverloft.domain.BoardDTO;
import iducs.springboot.weaverloft.domain.MemberDTO;
import iducs.springboot.weaverloft.service.BoardService;
import iducs.springboot.weaverloft.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller // 데이터(처리결과)를 view 에게 전달
//@RestController // 자원의 상태(응답)를 Client 에게 전달,
//Rest (Representational State Transfer), OpenAPIs
@RequestMapping("/")
public class HomeController {

    private final BoardService boardService;

    public HomeController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping("")
    public String getHome(Model model) {
        List<BoardDTO> boardDTOList = boardService.getAll();
        List<BoardDTO> gongList = new ArrayList<>();
        for (BoardDTO dto : boardDTOList) {
            BoardDTO boardDTO = boardService.getById(dto.getBno());
            if (boardDTO.getNotice_yn().equals("y")) {
                gongList.add(boardDTO);
            }
        }
        model.addAttribute("gongList", gongList);
        return "index";
    }

    @GetMapping("index2")
    public String getIndex2() {
        return "index2"; //view name
    }

    @GetMapping("index3")
    public String getIndex3() {
        return "index3"; //view name
    }

    @GetMapping("pages/tables/simple")
    public String getSimpleTables() {
        return "/pages/tables/simple"; //view name
    }

    @GetMapping("pages/contacts")
    public String getContacts() {
        return "/pages/contacts"; //view name
    }

}
