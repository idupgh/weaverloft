package iducs.springboot.board201812064;

import iducs.springboot.weaverloft.Board201812064Application;
import iducs.springboot.weaverloft.domian.Board;
import iducs.springboot.weaverloft.repository.BoardRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

@SpringBootTest(classes = Board201812064Application.class)
@Log4j2
public class BoardRepositoryTest {
    @Autowired
    private BoardRepository boardRepository;

    @AfterEach
    public void clear() {
        boardRepository.deleteAll();
    }

    @Test
    public void createBoardAndGetBoard() {
        String title = "제목 입니다.";
        String content = "내용 입니다";

        boardRepository.save(Board.builder().title(title).content(content).writer("hojunnnnn").build());

        List<Board> boardList = boardRepository.findAll();

        Board board = boardList.get(0);

        assertThat(board.getTitle()).isEqualTo(title);
        assertThat(board.getContent()).isEqualTo(content);

        log.info(board);
    }
}
