package iducs.springboot.weaverloft.service;


import iducs.springboot.weaverloft.domain.BoardDTO;
import iducs.springboot.weaverloft.domain.PageRequestDTO;
import iducs.springboot.weaverloft.domain.PageResultDTO;
import iducs.springboot.weaverloft.domain.ReplyDTO;
import iducs.springboot.weaverloft.entity.BoardEntity;
import iducs.springboot.weaverloft.entity.MemberEntity;
import org.springframework.transaction.annotation.Transactional;

public interface BoardService {
    Long register(BoardDTO boardDTO);  // Board : boardDTO or Domain

    PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO);
    BoardDTO getById(Long bno); // Id는 primary key 값 : @ID 필드
    Long modify(BoardDTO boardDTO);
    void DeleteById(Long bno);
    void deleteWithReplies(Long bno); //삭제

    default BoardEntity dtoToEntity(BoardDTO boardDTO) {
        MemberEntity member = MemberEntity.builder()
                .seq(boardDTO.getWriterSeq())
                .id(boardDTO.getWriterId())
                .build();
        BoardEntity board = BoardEntity.builder()
                .bno(boardDTO.getBno())
                .title(boardDTO.getTitle())
                .content(boardDTO.getContent())
                .writer(member)
                .block(boardDTO.getBlock())
                .views(boardDTO.getViews())
                .replyCount(boardDTO.getReplyCount())
                .fileId(boardDTO.getFileId())
                .build();
        return board;
    }
    default BoardDTO entityToDto(BoardEntity entity, MemberEntity member, Long replyCount) {
        BoardDTO boardDTO = BoardDTO.builder()
                .bno(entity.getBno())
                .title(entity.getTitle())
                .content(entity.getContent())
                .writerSeq(member.getSeq())
                .writerId(member.getId())
                .writerName(member.getName())
                .writerEmail(member.getEmail())
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate())
                .replyCount(replyCount.intValue())
                .block(entity.getBlock())
                .views(entity.getViews())
                .fileId(entity.getFileId())
                .build();
        return boardDTO;
    }

    void deleteById(BoardDTO boardDTO);

    BoardDTO readById(Long seq);

    void deleteByBno(Long bno);

    Long update(Long bno, BoardDTO boardDTO);

    int updateView(Long bno);
}
