package iducs.springboot.weaverloft.repository;

import iducs.springboot.weaverloft.entity.BoardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardRepository extends JpaRepository<BoardEntity, Long>, SearchBoardRepository {

    //JPQL : Java Persistence Query Language
    // 게시글 작성자로 조회
    @Query("select b, w from BoardEntity b left join b.writer w where b.bno =:bno")
    Object getBoardWithWriter(@Param("bno") Long bno);

    // 목록 조회를 위한 JPQL
    @Query(value = "select b, w, count(r) " +
            "from BoardEntity b left join b.writer w " +
            "left join ReplyEntity r on r.board = b " +
            "group by b",
            countQuery = "select count(b) from BoardEntity b")
    Page<Object[]> getBoardWithReplyCount(Pageable pageable); ///////////////////////////////////////////////

    // 게시글 번호로 게시글, 작성자 및 댓글수 조회
    @Query(value = "select b, w, count(r) " +
            "from BoardEntity b left join b.writer w " +
            "left join ReplyEntity r on r.board = b " +
            "where b.bno = :bno")
    Object getBoardByBno(@Param("bno") Long bno);

    // 게시글 번호로 게시글과 댓글 조회
    @Query("select b, r from BoardEntity b left join ReplyEntity r on r.board = b where b.bno = :bno")
    Object getBoardWithReply(@Param("bno") Long bno);

    // 게시글 조회수 처리
    @Modifying
    @Query("update BoardEntity b set b.views = b.views + 1 where b.bno = :bno")
    int updateView(@Param("bno") Long bno);
}
