package iducs.springboot.board201812064.domain;

import lombok.*;

import java.time.LocalDateTime;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class Board {
    private Long bno; // no? seq?
    private String title;
    private String content;

    private Long writerSeq;
    private String writerId;
    private String writerName;
    private String writerEmail;

    private LocalDateTime regDate;
    private LocalDateTime modDate;

    private int replyCount; // 게시물 댓글 수
}
