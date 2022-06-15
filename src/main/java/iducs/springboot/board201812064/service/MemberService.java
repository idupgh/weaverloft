package iducs.springboot.board201812064.service;

import iducs.springboot.board201812064.domain.Member;
import iducs.springboot.board201812064.domain.PageRequestDTO;
import iducs.springboot.board201812064.domain.PageResultDTO;
import iducs.springboot.board201812064.entity.MemberEntity;

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
}
