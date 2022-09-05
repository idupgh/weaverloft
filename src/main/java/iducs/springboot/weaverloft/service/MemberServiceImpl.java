package iducs.springboot.weaverloft.service;


import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import iducs.springboot.weaverloft.domain.MemberDTO;
import iducs.springboot.weaverloft.domain.PageRequestDTO;
import iducs.springboot.weaverloft.domain.PageResultDTO;
import iducs.springboot.weaverloft.entity.BoardEntity;
import iducs.springboot.weaverloft.entity.MemberEntity;
import iducs.springboot.weaverloft.entity.QMemberEntity;
import iducs.springboot.weaverloft.repository.BoardRepository;
import iducs.springboot.weaverloft.repository.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

// @Component, @Configuration, @Beans
// @Service, @Repository
@Service
public class MemberServiceImpl implements MemberService{
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;

    public MemberServiceImpl(BoardRepository boardRepository, MemberRepository memberRepository){
        this.boardRepository = boardRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    public void create(MemberDTO memberDTO) {
        MemberEntity entity = dtoToEntity(memberDTO);
                /*
                .seq(member.getSeq())
                .id(member.getId())
                .pw(member.getPw())
                .name(member.getName())
                .email(member.getEmail())
                .phone(member.getPhone())
                .address(member.getAddress())
                .build();
                 */
        // new MemoEntity(seq, id, pw, name, email, phone, address)
        memberRepository.save(entity);
    }

    private MemberEntity dtoToEntity(MemberDTO memberDTO) {
        MemberEntity entity = MemberEntity.builder()
                .seq(memberDTO.getSeq())
                .id(memberDTO.getId())
                .pw(memberDTO.getPw())
                .name(memberDTO.getName())
                .email(memberDTO.getEmail())
                .phone(memberDTO.getPhone())
                .address(memberDTO.getAddress())
                .block(memberDTO.getBlock())
                .build();
        return entity;
    }

    @Override
    public MemberDTO readById(Long seq) {
        MemberDTO memberDTO = null;
        Optional<MemberEntity> result = memberRepository.findById(seq);
        if(result.isPresent()) {
            memberDTO = entityToDto(result.get());
        }
        return memberDTO;
    }

    // Service -> Controller : entity -> dto 로 변환후 반환
    private MemberDTO entityToDto(MemberEntity entity) {
        MemberDTO memberDTO = MemberDTO.builder()
                .seq(entity.getSeq())
                .id(entity.getId())
                .pw(entity.getPw())
                .name(entity.getName())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .address(entity.getAddress())
                .block(entity.getBlock())
                .build();
        return memberDTO;
    }

    @Override
    public List<MemberDTO> readAll() {
        List<MemberDTO> members = new ArrayList<MemberDTO>(); //반환 리스트객체
        List<MemberEntity> entities = memberRepository.findAll(); //entity 들
        for(MemberEntity entity : entities) {
            MemberDTO memberDTO = entityToDto(entity);
            members.add(memberDTO);
        }
        return members;
    }

    @Override
    public void update(MemberDTO memberDTO) {
        MemberEntity entity = dtoToEntity(memberDTO);
        memberRepository.save(entity);
    }

    @Override
    public void delete(MemberDTO memberDTO) {
        MemberEntity entity = dtoToEntity(memberDTO);
        memberRepository.deleteById(entity.getSeq());
    }

    @Override
    public PageResultDTO<MemberDTO, MemberEntity> readListBy(PageRequestDTO pageRequestDTO) {
        Sort sort = Sort.by("seq").descending();
        if(pageRequestDTO.getSort() == null)
            sort = Sort.by("seq").descending();
        else if(pageRequestDTO.getSort().equals("asc"))
            sort = Sort.by("seq").ascending();
        Pageable pageable = pageRequestDTO.getPageable(sort);
        BooleanBuilder booleanBuilder = findByCondition(pageRequestDTO);

        Page<MemberEntity> result = memberRepository.findAll(booleanBuilder, pageable);

        // Page<MemberEntity> result = memberRepository.findAll(pageable);
        Function<MemberEntity, MemberDTO> fn = (entity -> entityToDto(entity));

        return new PageResultDTO<>(result, fn);
    }
    private BooleanBuilder findByCondition(PageRequestDTO pageRequestDTO) {
        String type = pageRequestDTO.getType();
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QMemberEntity qMemberEntity = QMemberEntity.memberEntity;

        BooleanExpression expression = qMemberEntity.seq.gt(0L); // where seq > 0 and title == "title"
        booleanBuilder.and(expression);

        if(type == null || type.trim().length() == 0) {
            return booleanBuilder;
        }

        String keyword = pageRequestDTO.getKeyword();

        BooleanBuilder conditionBuilder = new BooleanBuilder();
        if(type.contains("e")) // email 로 검색
            conditionBuilder.or(qMemberEntity.email.contains(keyword));
        if(type.contains("p")) // phone 로 검색
            conditionBuilder.or(qMemberEntity.phone.contains(keyword));
        if(type.contains("a")) // address 로 검색
            conditionBuilder.or(qMemberEntity.address.contains(keyword));
        booleanBuilder.and(conditionBuilder);
        return booleanBuilder; // 완성된 조건 or 술어
    }

    @Override
    public MemberDTO readByName(MemberDTO memberDTO) {
        return null;
    }

    @Override
    public MemberDTO readByEmail(String member) {
        return null;
    }

    @Override
    public MemberDTO loginByEmail(MemberDTO member) {
        MemberDTO memberDTO = null;
        Object result = memberRepository.getMemberByEmail(member.getEmail(), member.getPw());
        if(result != null){
            memberDTO = entityToDto((MemberEntity) result);
        }
        return memberDTO;
    }

    @Override
    public void removeWithBoards(Long seq) {
        MemberEntity byId = memberRepository.getById(seq);

        List<BoardEntity> boardEntities = byId.getBoardEntities();

        Iterator b = boardEntities.iterator();
        while (b.hasNext()){ //값이 있으면 true 없으면 false
            Object next = b.next();
            boardRepository.delete((BoardEntity)next);
        }

    }
}