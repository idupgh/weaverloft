package iducs.springboot.board201812064.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="tbl_reply")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "board")
public class ReplyEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rno;

    private String text;
    private String replier;

    @ManyToOne
    private BoardEntity board; // 연관관계 지정
}
