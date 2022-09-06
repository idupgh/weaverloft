package iducs.springboot.weaverloft.controller;

import iducs.springboot.weaverloft.dto.BoardResponseDto;
import iducs.springboot.weaverloft.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 *  화면 연결 Controller
 */

@Controller
@RequiredArgsConstructor
public class BoardIndexController {

    private final BoardService boardService;

    @GetMapping("/board/write")
    public String write() {
        return "/boards/regform";
    }

    @GetMapping("/board/read/{id}")
    public String read(@PathVariable Long id, Model model) {
        BoardResponseDto dto = boardService.findById(id);
        model.addAttribute("board", dto);

        return "/boards/read";
    }

    @GetMapping("/posts/update/{id}")
    public String update(@PathVariable Long id, Model model) {
        BoardResponseDto dto = boardService.findById(id);
        model.addAttribute("board", dto);

        return "/boards/upform";
    }
}
