package iducs.springboot.weaverloft.domain;

import lombok.Builder;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data //@Getter, @Setter, @EqualsAndHash, @RequiredArgsConstructor
@Builder
public class MemberDTO { // DTO(Data Transform Object) : Client <-> Controller <-> Service
    private Long seq; //seq 번호, 자동 증가하는 유일키

    @NotBlank(message = "아이디는 필수 입력 값입니다.")
    private String id;

    @NotBlank(message = "비밀번호는는 필수 입력 입니다.")
    private String pw;

    private String name;

    @Email(message = "올바른 이메일 주소를 입력해주세요.")
    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    private String email;

    private String phone;

    private String address;

    private String block = "unblock"; // 차단 여부

}

