package iducs.springboot.weaverloft.domian;

import lombok.*;

import javax.persistence.*;

@Table(name="new_board")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
@ToString
public class Board extends TimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private String writer;

    private int view; // 조회수

    public void update(String title, String writer, String content) {
        this.title = title;
        this.writer = writer;
        this.content = content;
    }
}
