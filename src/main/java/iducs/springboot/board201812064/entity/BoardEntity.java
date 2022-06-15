package iducs.springboot.board201812064.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="tbl_board")
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

    @ManyToOne
    private MemberEntity writer; // 연관관계 지정
}
