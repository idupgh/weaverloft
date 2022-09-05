package iducs.springboot.weaverloft.repository;

import iducs.springboot.weaverloft.domain.BoardDTO;
import iducs.springboot.weaverloft.entity.BoardEntity;
import iducs.springboot.weaverloft.entity.ReplyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReplyRepository extends JpaRepository<ReplyEntity, Long> {

    // Board 삭제시 댓글들 삭제
    @Modifying
    @Query("delete from ReplyEntity r where r.board.bno =:bno ")
    void deleteByBno(@Param("bno")Long bno);

    // 게시물로 댓글 목록 가져오기
    List<ReplyEntity> getRepliesByBoardOrderByRno(BoardEntity board);
}
