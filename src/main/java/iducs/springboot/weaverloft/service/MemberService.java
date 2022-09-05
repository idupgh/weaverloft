package iducs.springboot.weaverloft.service;

import iducs.springboot.weaverloft.domain.Member;
import iducs.springboot.weaverloft.domain.PageRequestDTO;
import iducs.springboot.weaverloft.domain.PageResultDTO;
import iducs.springboot.weaverloft.entity.MemberEntity;

import java.util.List;

public interface MemberService {

    void create(Member member);
    Member readById(Long seq);
    List<Member> readAll();
    void update(Member member);
    void delete(Member member);
    PageResultDTO<Member, MemberEntity> readListBy(PageRequestDTO pageRequestDTO);
    Member readByName(Member member);
    Member readByEmail(String member);

    Member loginByEmail(Member member);

    void removeWithBoards(Long seq);
}
