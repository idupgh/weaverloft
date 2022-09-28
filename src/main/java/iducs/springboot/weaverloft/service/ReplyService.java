package iducs.springboot.weaverloft.service;

import iducs.springboot.weaverloft.domain.ReplyDTO;
import iducs.springboot.weaverloft.entity.BoardEntity;
import iducs.springboot.weaverloft.entity.MemberEntity;
import iducs.springboot.weaverloft.entity.ReplyEntity;

import java.util.List;

public interface ReplyService {
    Long register(ReplyDTO replyDTO); //댓글 등록

    List<ReplyDTO> getList(Long bno); // 특정 게시물의 댓글 목록

    void modify(ReplyDTO replyDTO); //댓글 수정

    ReplyDTO remove(Long rno);//댓글 삭제\

    // ReplyDTO를 Reply 객체로 변환 Board객체의 처리가 수반된다
    default ReplyEntity dtoToEntity(ReplyDTO replyDTO){

        BoardEntity board = BoardEntity.builder().bno(replyDTO.getBno()).build();
        MemberEntity member = MemberEntity.builder().id(replyDTO.getId()).build();

        ReplyEntity reply = ReplyEntity.builder()
                .rno(replyDTO.getRno())
                .text(replyDTO.getText())
                .board(board)
                .member(member)
                .build();

        return reply;
    }

    //Reply 객체를 ReplyDTO 로 변환 Board 객체가 필요하지 않으므로 게시물 번호만
    default ReplyDTO entityToDTO(ReplyEntity reply){

        ReplyDTO replyDTO = ReplyDTO.builder()
                .rno(reply.getRno())
                .text(reply.getText())
                .regDate(reply.getRegDate())
                .modDate(reply.getModDate())
                .build();

        return replyDTO;
    }

    // ReplyDTO getReply(Long rno);
}
