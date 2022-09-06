package iducs.springboot.weaverloft.repository;

import iducs.springboot.weaverloft.domian.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    @Query("select p from Board p order by p.id desc")
    List<Board> findAllDesc();
}
