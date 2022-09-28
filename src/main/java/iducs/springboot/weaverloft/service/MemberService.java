package iducs.springboot.weaverloft.service;

import iducs.springboot.weaverloft.domain.MemberDTO;
import iducs.springboot.weaverloft.domain.PageRequestDTO;
import iducs.springboot.weaverloft.domain.PageResultDTO;
import iducs.springboot.weaverloft.entity.MemberEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
public interface MemberService {

    void create(MemberDTO memberDTO);
    MemberDTO readById(String id);
    List<MemberDTO> readAll();
    void update(MemberDTO memberDTO);
    void delete(MemberDTO memberDTO);
    PageResultDTO<MemberDTO, MemberEntity> readListBy(PageRequestDTO pageRequestDTO);
    MemberDTO readByName(MemberDTO memberDTO);
    MemberDTO readByEmail(String member);

    MemberDTO loginById(MemberDTO memberDTO);

    MemberDTO loginByDelete(MemberDTO member, String delete);

    void removeWithBoards(String id);

    @Transactional(readOnly = true)
    boolean checkUsernameDuplication(String name);

    @Transactional(readOnly = true)
    boolean checkIdDuplication(String id);

    @Transactional(readOnly = true)
    public void validateDuplicateMember(MemberEntity memberEntity);

    /* 회원가입 시, 유효성 및 중복 검사 */
    @Transactional(readOnly = true)
    public default Map<String, String> validateHandling(Errors errors) {
        Map<String, String> validatorResult = new HashMap<>();
        /* 유효성 및 중복 검사에 실패한 필드 목록을 받음 */
        for (FieldError error : errors.getFieldErrors()) {
            String validKeyName = String.format("valid_%s", error.getField());
            validatorResult.put(validKeyName, error.getDefaultMessage());
        }
        return validatorResult;
    }

    @Transactional
    void deleteMember(String id);
}
