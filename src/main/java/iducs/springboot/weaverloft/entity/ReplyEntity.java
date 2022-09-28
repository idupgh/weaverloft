package iducs.springboot.weaverloft.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="tb_reply")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "board")
public class ReplyEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long rno;

    @Column(nullable = false)
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    private BoardEntity board; // 연관관계 지정

    @ManyToOne(fetch = FetchType.LAZY)
    private MemberEntity member; // 연관관계 지정
}
