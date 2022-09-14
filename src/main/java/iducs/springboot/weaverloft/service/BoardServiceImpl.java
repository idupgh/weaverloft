package iducs.springboot.weaverloft.service;

import iducs.springboot.weaverloft.domain.BoardDTO;
import iducs.springboot.weaverloft.domain.PageRequestDTO;
import iducs.springboot.weaverloft.domain.PageResultDTO;
import iducs.springboot.weaverloft.entity.BoardEntity;
import iducs.springboot.weaverloft.entity.MemberEntity;
import iducs.springboot.weaverloft.repository.BoardRepository;
import iducs.springboot.weaverloft.repository.FileRepository;
import iducs.springboot.weaverloft.repository.ReplyRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.function.Function;

@Service
@Log4j2

public class BoardServiceImpl implements BoardService {
    private final BoardRepository boardRepository;
    private final ReplyRepository replyRepository; //추가

    private final FileRepository fileRepository;

    public BoardServiceImpl(BoardRepository boardRepository, ReplyRepository replyRepository, FileRepository fileRepository) {
        this.boardRepository = boardRepository;
        this.replyRepository = replyRepository;
        this.fileRepository = fileRepository;
    }

    @Override
    public Long register(BoardDTO boardDTO) { // Controller -> 객체 -> Service
        log.info(boardDTO);
        BoardEntity boardEntity = dtoToEntity(boardDTO);
        boardRepository.save(boardEntity);
        return boardEntity.getBno();// 게시물 번호
    }

    @Override
    public PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO) {
        log.info(">>>>" + pageRequestDTO);

        Function<Object[], BoardDTO> fn = (entity -> entityToDto((BoardEntity) entity[0],
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
    public BoardDTO getById(Long bno) {
        Object result = boardRepository.getBoardByBno(bno); // 기본 CRUD 는 JpaRepository 에서 제공
        Object[] en = (Object[]) result;
        return entityToDto((BoardEntity) en[0], (MemberEntity) en[1], (Long) en[2]);
    }

    @Override
    public Long modify(BoardDTO boardDTO) {
        return null;
    }

    @Override
    public void DeleteById(Long bno) {

    }

    @Override
    public BoardEntity dtoToEntity(BoardDTO boardDTO) {
        return BoardService.super.dtoToEntity(boardDTO);
    }

    @Override
    public BoardDTO entityToDto(BoardEntity entity, MemberEntity member, Long replyCount) {
        return BoardService.super.entityToDto(entity, member, replyCount);
    }

    @Override
    public void deleteById(BoardDTO boardDTO) {
        BoardEntity entity = dtoToEntity(boardDTO);
        boardRepository.deleteById(entity.getBno());
    }


    @Override
    public BoardDTO readById(Long seq) {
        BoardDTO boardDTO = null;
        Optional<BoardEntity> result = boardRepository.findById(seq);
        if (result.isPresent()) {
            boardDTO = entityToDto(result.get());
        }
        return boardDTO;
    }

    @Override
    public void deleteByBno(Long bno) {
        BoardEntity boardEntity = boardRepository.findById(bno).orElseThrow(()->
                new IllegalArgumentException("해당 게시글이 없습니다. bno = "+ bno));
        boardRepository.delete(boardEntity);
    }

    @Override
    public Long update(Long bno, BoardDTO boardDTO) {
        BoardEntity boardEntity = dtoToEntity(boardDTO);
        boardRepository.save(boardEntity);
        return bno;
    }


    private BoardDTO entityToDto(BoardEntity entity) {
        BoardDTO boardDTO = BoardDTO.builder()
                .bno(entity.getBno())
                .modDate(entity.getModDate())
                .regDate(entity.getRegDate())
                .content(entity.getContent())
                .title(entity.getTitle())
                .writerSeq(entity.getWriter().getSeq())
                .fileId(entity.getFileId())
                .build();
        return boardDTO;
    }

    @Transactional
    public void deleteWithReplies(Long bno) { // 삭제 구현, 트랜잭션 추가

        // 파일도 같이 삭제
        fileRepository.deleteByBno(bno);

        // 댓글부터 삭제
        replyRepository.deleteByBno(bno);

        // 게시글 삭제
        boardRepository.deleteById(bno);
    }

    @Transactional
    @Override
    public int updateView(Long bno) {
        return boardRepository.updateView(bno);
    }
}
