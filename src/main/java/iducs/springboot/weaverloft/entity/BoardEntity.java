package iducs.springboot.weaverloft.entity;

import lombok.*;
import lombok.Builder.Default;

import javax.persistence.*;

@Entity
@Table(name="tb_board")
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

    @Column(columnDefinition = "integer default 0", nullable = false)
    private int views;

    @Column
    private Long fileId;


    private int replyCount;
}
