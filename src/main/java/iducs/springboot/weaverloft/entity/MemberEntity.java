package iducs.springboot.weaverloft.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

// Spring Data 관련 Annotation
@Entity // Spring Data JPA의 엔티티(entity, 개체)임을 나타냄
@Table(name="tb_member")
// Lombok Annotation
@ToString // toString() 생성
@Getter // getter() 생성
@Setter //setter() 추가
@Builder
@AllArgsConstructor // 모든 매개변수를 갖는 생성자
@NoArgsConstructor // 디폴트 생성자(아무런 매개변수가 없는)

public class MemberEntity extends BaseEntity {


    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Id
    @Column(length = 50, nullable = false)
    private String id;

    @Column(length = 50, nullable = false)
    private String pw;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(length = 50, nullable = false)
    private String email;
    @Column(length = 50, nullable = true)
    private String phone;
    @Column(length = 100, nullable = true)
    private String address;

    @Builder.Default
    private String block = "unblock"; // 차단 여부

    @Builder.Default
    private String delete_yn = "n";

    @Builder.Default
    @OneToMany(mappedBy = "writer") //값을 수정하고 삭제 할수없음 조회만 가능
    private List<BoardEntity> boardEntities = new ArrayList<>();
}
