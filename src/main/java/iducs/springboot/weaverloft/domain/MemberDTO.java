package iducs.springboot.weaverloft.domain;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data //@Getter, @Setter, @EqualsAndHash, @RequiredArgsConstructor
@Builder
public class MemberDTO { // DTO(Data Transform Object) : Client <-> Controller <-> Service
    private Long seq; //seq 번호, 자동 증가하는 유일키

    @NotBlank(message = "아이디는 필수 입력 값입니다.")
    @Pattern(regexp = "^(?!admin).*$", message = "ID 에 admin은 포함할 수 없습니다.")
    @Pattern(regexp = "^[a-z0-9]{4,20}$", message = "아이디는 영어 소문자와 숫자만 사용하여 4~20자리여야 합니다.")
    private String id;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String pw;

    @NotBlank(message = "이름을 입력해주세요.")
    @Pattern(regexp = "^[가-힣]{2,10}$" , message = "이름은 한글 2~10자리여야 합니다.")
    private String name;

    @Email(message = "올바른 이메일 주소를 입력해주세요.")
    @NotBlank(message = "이메일은 입력해주세요.")
    private String email;

    @NotBlank(message = "휴대폰 번호를 입력해주세요.")
    @Pattern(regexp = "(01[016789])(\\d{3,4})(\\d{4})", message = "올바른 휴대폰 번호를 입력해주세요.")
    private String phone;

    private String address;

    @Builder.Default
    private String block = "unblock"; // 차단 여부

    @Builder.Default
    private String delete_yn = "n";

    @Builder.Default
    private String role = "user";

}

