package iducs.springboot.weaverloft.controller;

import iducs.springboot.weaverloft.domain.MemberDTO;
import iducs.springboot.weaverloft.repository.MemberRepository;
import iducs.springboot.weaverloft.service.MemberService;
import iducs.springboot.weaverloft.service.MemberServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

/*@RequiredArgsConstructor
@Component*/

/*public class CheckIdValidator extends AbstractValidator<MemberDTO> {
    private final MemberRepository memberRepository;

    @Override
    protected void doValidate(String id, Errors errors) {
        if (memberRepository.existsById(String id) {
            errors.rejectValue("id", "이메일 중복 오류", "이미 사용중인 이메일 입니다.");
        }
    }
}*/
