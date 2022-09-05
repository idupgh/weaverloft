package iducs.springboot.weaverloft.service;

import iducs.springboot.weaverloft.domain.BoardDTO;
import iducs.springboot.weaverloft.domain.ReplyDTO;
import iducs.springboot.weaverloft.entity.BoardEntity;
import iducs.springboot.weaverloft.entity.ReplyEntity;
import iducs.springboot.weaverloft.repository.ReplyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReplyServiceImpl implements ReplyService{

    private final ReplyRepository replyRepository;

    public ReplyServiceImpl(ReplyRepository replyRepository) {
        this.replyRepository = replyRepository;
    }


    @Override
    public Long register(ReplyDTO replyDTO) {

        ReplyEntity reply = dtoToEntity(replyDTO);

        replyRepository.save(reply);

        return reply.getRno();
    }

    @Override
    public List<ReplyDTO> getList(Long bno) {

        List<ReplyEntity> result = replyRepository.getRepliesByBoardOrderByRno(BoardEntity.builder().bno(bno).build());

        return result.stream().map(reply -> entityToDTO(reply)).collect(Collectors.toList());
    }

    @Override
    public void modify(ReplyDTO replyDTO) {

        ReplyEntity reply = dtoToEntity(replyDTO);

        replyRepository.save(reply);

    }

    @Override
    public void remove(Long rno) {

        replyRepository.deleteById(rno);

    }
}
