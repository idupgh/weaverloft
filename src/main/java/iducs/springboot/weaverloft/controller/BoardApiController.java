package iducs.springboot.weaverloft.controller;

import iducs.springboot.weaverloft.dto.BoardRequestDto;
import iducs.springboot.weaverloft.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * REST API Controller
 */

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class BoardApiController {

    private final BoardService boardService;

    /* CREATE */
    @PostMapping("/board")
    public ResponseEntity<Long> write(@RequestBody BoardRequestDto dto) {
        Long id = boardService.write(dto);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    /* UPDATE */
    @PutMapping("/board/{id}")
    public ResponseEntity<Long> update(@PathVariable Long id, @RequestBody BoardRequestDto requestDto) {
        if (id == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(boardService.update(id, requestDto));
        }
    }

    /* List */
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("posts", postsService.findAllDesc());
        return "index";
    }
}
