package iducs.springboot.weaverloft.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="tb_board_201812064")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "writer")
public class BoardEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;

    private String title;
    private String content;

    private String block = "unblock";

    @ManyToOne
    private MemberEntity writer; // 연관관계 지정

    @Column(columnDefinition = "BIGINT(20) default 0", nullable = false)
    private Long views;
}
