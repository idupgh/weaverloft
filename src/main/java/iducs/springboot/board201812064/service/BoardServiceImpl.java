package iducs.springboot.board201812064.service;

import iducs.springboot.board201812064.domain.Board;
import iducs.springboot.board201812064.domain.PageRequestDTO;
import iducs.springboot.board201812064.domain.PageResultDTO;
import iducs.springboot.board201812064.entity.BoardEntity;
import iducs.springboot.board201812064.entity.MemberEntity;
import iducs.springboot.board201812064.repository.BoardRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;

@Service
@Log4j2

public class BoardServiceImpl implements BoardService {
    private final BoardRepository boardRepository;

    public BoardServiceImpl(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @Override
    public Long register(Board dto) { // Controller -> 객체 -> Service
        log.info(dto);
        BoardEntity boardEntity = dtoToEntity(dto);
        boardRepository.save(boardEntity);
        return boardEntity.getBno();// 게시물 번호
    }

    @Override
    public PageResultDTO<Board, Object[]> getList(PageRequestDTO pageRequestDTO) {
        log.info(">>>>" + pageRequestDTO);

        Function<Object[], Board> fn = (entity -> entityToDto((BoardEntity) entity[0],
                (MemberEntity) entity[1],(Long) entity[2]));
        String type = pageRequestDTO.getType();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = null;
        String page = pageRequestDTO.getSort();
        String asc = "asc";
        pageable = pageRequestDTO.getPageable(Sort.by("bno").descending());
        if(page != null) {
            if(page.equals(asc)) {
                pageable = pageRequestDTO.getPageable(Sort.by("bno").ascending());
            }
        }

        Page<Object[]> result = boardRepository.searchPage(type, keyword, pageable);

        return new PageResultDTO<>(result, fn);
    }

    @Override
    public Board getById(Long bno) {
        Object result = boardRepository.getBoardByBno(bno); // 기본 CRUD 는 JpaRepository 에서 제공
        Object[] en = (Object[]) result;
        return entityToDto((BoardEntity) en[0], (MemberEntity) en[1], (Long) en[2]);
    }

    @Override
    public Long modify(Board dto) {
        return null;
    }

    @Override
    public void DeleteById(Long bno) {

    }

    @Override
    public BoardEntity dtoToEntity(Board dto) {
        return BoardService.super.dtoToEntity(dto);
    }

    @Override
    public Board entityToDto(BoardEntity entity, MemberEntity member, Long replyCount) {
        return BoardService.super.entityToDto(entity, member, replyCount);
    }

    @Override
    public void deleteById(Board dto) {
        BoardEntity entity = dtoToEntity(dto);
        boardRepository.deleteById(entity.getBno());
    }


    @Override
    public Board readById(Long seq) {
        Board board = null;
        Optional<BoardEntity> result = boardRepository.findById(seq);
        if (result.isPresent()) {
            board = entityToDto(result.get());
        }
        return board;
    }

    @Override
    public void deleteByBno(Long bno) {
        BoardEntity boardEntity = boardRepository.findById(bno).orElseThrow(()->
                new IllegalArgumentException("해당 게시글이 없습니다. bno = "+ bno));
        boardRepository.delete(boardEntity);
    }

    @Override
    public Long update(Long bno, Board boardDTO) {
        BoardEntity boardEntity = dtoToEntity(boardDTO);
        boardRepository.save(boardEntity);
        return bno;
    }


    private Board entityToDto(BoardEntity entity) {
        Board board = Board.builder()
                .bno(entity.getBno())
                .modDate(entity.getModDate())
                .regDate(entity.getRegDate())
                .content(entity.getContent())
                .title(entity.getTitle())
                .writerSeq(entity.getWriter().getSeq())
                .build();
        return board;
    }
}
