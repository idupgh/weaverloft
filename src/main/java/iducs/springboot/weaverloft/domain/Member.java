package iducs.springboot.weaverloft.domain;

import lombok.Builder;
import lombok.Data;

@Data //@Getter, @Setter, @EqualsAndHash, @RequiredArgsConstructor
@Builder
public class Member { // DTO(Data Transform Object) : Client <-> Controller <-> Service
    private Long seq; //seq 번호, 자동 증가하는 유일키
    private String id;
    private String pw;
    private String name;
    private String email;
    private String phone;
    private String address;

    private String block = "unblock"; // 차단 여부

}

