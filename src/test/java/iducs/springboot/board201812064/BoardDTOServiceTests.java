package iducs.springboot.board201812064;

import iducs.springboot.weaverloft.Board201812064Application;
import iducs.springboot.weaverloft.domain.BoardDTO;
import iducs.springboot.weaverloft.domain.PageRequestDTO;
import iducs.springboot.weaverloft.domain.PageResultDTO;
import iducs.springboot.weaverloft.service.BoardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.IntStream;

@SpringBootTest(classes = Board201812064Application.class)
public class BoardDTOServiceTests {
    @Autowired
    BoardService boardService;


    @Test
    public void testRegister() { //47개의 BoardDTO 데이터 만들기
        IntStream.rangeClosed(1, 50).forEach(i -> {
            BoardDTO dto = BoardDTO.builder()
                    .title("Test.")
                    .content("Content")
                    .writerSeq(Long.valueOf("" + i))
                    .block("unblock")
                    .build();
            Long bno = boardService.register(dto);
        });
    }

    @Test
    public void testRegisterOne() {
            BoardDTO dto = BoardDTO.builder()
                    .title("Title")
                    .content("Content")
                    .writerSeq(10L)
                    .build();
            Long bno = boardService.register(dto);
    }

    @Test
    public void testList() {
        PageRequestDTO pageRequestDTO = new PageRequestDTO();
        pageRequestDTO.setPage(3);
        PageResultDTO<BoardDTO, Object[]> result = boardService.getList(pageRequestDTO);
        for(BoardDTO dto : result.getDtoList())
            System.out.println("ylist" + dto);
    }
}
