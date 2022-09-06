package iducs.springboot.board201812064;

import iducs.springboot.weaverloft.Board201812064Application;
import iducs.springboot.weaverloft.dto.BoardRequestDto;
import iducs.springboot.weaverloft.repository.BoardRepository;
import iducs.springboot.weaverloft.service.BoardService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = Board201812064Application.class)
@Log4j2
public class BoardServiceTest {

    @Autowired
    BoardService boardService;

    @Autowired
    BoardRepository boardRepository;

    @AfterEach
    public void clear() {
        boardRepository.deleteAll();
    }

    @Test
    public void 게시글_생성() {
        BoardRequestDto board = BoardRequestDto.builder()
                .title("Test Title")
                .writer("user")
                .content("Test Content")
                .view(0)
                .build();

        boardService.write(board);

        log.info(board);
    }
}
