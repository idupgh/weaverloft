package iducs.springboot.board201812064.repository;

import iducs.springboot.board201812064.entity.BoardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchBoardRepository {
    BoardEntity searchBoard();

    Page<Object[]> searchPage(String type, String keyword, Pageable pageable);
}
