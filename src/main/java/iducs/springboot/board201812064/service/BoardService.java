package iducs.springboot.board201812064.service;


import iducs.springboot.board201812064.domain.Board;
import iducs.springboot.board201812064.domain.PageRequestDTO;
import iducs.springboot.board201812064.domain.PageResultDTO;
import iducs.springboot.board201812064.entity.BoardEntity;
import iducs.springboot.board201812064.entity.MemberEntity;

public interface BoardService {
    Long register(Board dto);  // Board : DTO or Domain

    PageResultDTO<Board, Object[]> getList(PageRequestDTO pageRequestDTO);
    Board getById(Long bno); // Id는 primary key 값 : @ID 필드
    Long modify(Board dto);
    void DeleteById(Long bno);

    default BoardEntity dtoToEntity(Board dto) {
        MemberEntity member = MemberEntity.builder()
                .seq(dto.getWriterSeq())
                .build();
        BoardEntity board = BoardEntity.builder()
                .bno(dto.getBno())
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(member)
                .build();
        return board;
    }
    default Board entityToDto(BoardEntity entity, MemberEntity member, Long replyCount) {
        Board dto = Board.builder()
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
                .build();
        return dto;
    }

    void deleteById(Board dto);

    Board readById(Long seq);

    void deleteByBno(Long bno);

    Long update(Long bno, Board boardDTO);
}
