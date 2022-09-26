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
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;

// @Component, @Configuration, @Beans
// @Service, @Repository
@Transactional // jpa는 모든 데이터 변경이 transaction 안에서 실행되어야함
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
        validateDuplicateMember(entity);
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
                .delete_yn(memberDTO.getDelete_yn())
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
                .delete_yn(entity.getDelete_yn())
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
    public MemberDTO loginByDelete(MemberDTO member, String delete) {
        MemberDTO memberDTO = null;
        Object result = memberRepository.deleteById(member.getDelete_yn());
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

    @Transactional(readOnly = true)
    @Override
    public boolean checkUsernameDuplication(String name) {
        boolean nameDuplicate = memberRepository.existsByName(name);
        return nameDuplicate;
    }

    /* 아이디, 닉네임, 이메일 중복 여부 확인 */
    @Transactional(readOnly = true)
    public boolean checkIdDuplication(String id) {
        boolean idDuplicate = memberRepository.existsById(id);
        if (idDuplicate) {
            throw new IllegalStateException("이미 존재하는 아이디입니다.");
        }
        return idDuplicate;
    }

    @Transactional(readOnly = true)
    public void validateDuplicateMember(MemberEntity memberEntity) {
        MemberEntity findMember = memberRepository.findById(memberEntity.getId());
        if (findMember != null) {
            throw new IllegalStateException("이미 가입한 아이디입니다.");
        }
    }

    @Transactional
    @Override
    public void deleteMember(Long seq){
        Optional<MemberEntity> optMember = memberRepository.findById(seq);
        if(optMember.isPresent()){
            MemberEntity member = optMember.get();
            member.setDelete_yn("y");
        }
    }
}
