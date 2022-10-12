package iducs.springboot.weaverloft.domain;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Data //@Getter, @Setter, @EqualsAndHash, @RequiredArgsConstructor
@Builder
public class MemberDTO { // DTO(Data Transform Object) : Client <-> Controller <-> Service

    @Pattern(regexp = "^[a-z0-9]{4,20}$", message = "아이디는 영어 소문자와 숫자만 사용하여 4~20자리여야 합니다.")
    private String id;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String pw;

    @Pattern(regexp = "^[가-힣]{2,10}$" , message = "이름은 한글 2~10자리여야 합니다.")
    private String name;

    @Pattern(regexp = "^[a-z0-9A-Z._-]*@[a-z0-9A-Z]*.[a-zA-Z.]*$",message = "올바른 이메일 주소를 입력해주세요.")
    private String email;

    @Pattern(regexp = "(01[016789])(\\d{3,4})(\\d{4})", message = "-를 제외한 올바른 휴대폰 번호를 입력해주세요.")
    private String phone;

    private String address;

    @Builder.Default
    private String block = "unblock"; // 차단 여부

    @Builder.Default
    private String delete_yn = "n";

    @Builder.Default
    private String role = "user";

    private LocalDateTime pwUpdateDate;

}

