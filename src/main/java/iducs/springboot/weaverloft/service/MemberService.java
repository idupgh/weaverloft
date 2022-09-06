package iducs.springboot.weaverloft.service;

import iducs.springboot.weaverloft.domain.MemberDTO;
import iducs.springboot.weaverloft.domain.PageRequestDTO;
import iducs.springboot.weaverloft.domain.PageResultDTO;
import iducs.springboot.weaverloft.entity.MemberEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MemberService {

    void create(MemberDTO memberDTO);
    MemberDTO readById(Long seq);
    List<MemberDTO> readAll();
    void update(MemberDTO memberDTO);
    void delete(MemberDTO memberDTO);
    PageResultDTO<MemberDTO, MemberEntity> readListBy(PageRequestDTO pageRequestDTO);
    MemberDTO readByName(MemberDTO memberDTO);
    MemberDTO readByEmail(String member);

    MemberDTO loginByEmail(MemberDTO memberDTO);

    void removeWithBoards(Long seq);

    @Transactional(readOnly = true)
    boolean checkUsernameDuplication(String name);
}
