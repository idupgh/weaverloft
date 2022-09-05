package iducs.springboot.weaverloft.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="tb_reply_201812064")
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
