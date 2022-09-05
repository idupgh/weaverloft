package iducs.springboot.weaverloft.service;


import iducs.springboot.weaverloft.domain.BoardDTO;
import iducs.springboot.weaverloft.domain.PageRequestDTO;
import iducs.springboot.weaverloft.domain.PageResultDTO;
import iducs.springboot.weaverloft.entity.BoardEntity;
import iducs.springboot.weaverloft.entity.MemberEntity;

public interface BoardService {
    Long register(BoardDTO dto);  // Board : DTO or Domain

    PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO);
    BoardDTO getById(Long bno); // Id는 primary key 값 : @ID 필드
    Long modify(BoardDTO dto);
    void DeleteById(Long bno);
    void deleteWithReplies(Long bno); //삭제

    default BoardEntity dtoToEntity(BoardDTO dto) {
        MemberEntity member = MemberEntity.builder()
                .seq(dto.getWriterSeq())
                .build();
        BoardEntity board = BoardEntity.builder()
                .bno(dto.getBno())
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(member)
                .block(dto.getBlock())
                .build();
        return board;
    }
    default BoardDTO entityToDto(BoardEntity entity, MemberEntity member, Long replyCount) {
        BoardDTO dto = BoardDTO.builder()
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
                .build();
        return dto;
    }

    void deleteById(BoardDTO dto);

    BoardDTO readById(Long seq);

    void deleteByBno(Long bno);

    Long update(Long bno, BoardDTO boardDTO);
}
