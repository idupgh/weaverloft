package iducs.springboot.weaverloft.entity;

import lombok.*;
import lombok.Builder.Default;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
    @Column(nullable = false)
    private Long bno;

    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String block = "unblock";

    @ManyToOne
    private MemberEntity writer; // 연관관계 지정

    @Column(columnDefinition = "integer default 0", nullable = false)
    private int views;

    private Long fileId;

    private int replyCount;

}
