package iducs.springboot.weaverloft.domain;

import lombok.*;

import java.time.LocalDate;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class BoardDTO {
    private Long bno; // no? seq?
    private String title;
    private String content;

    private Long writerSeq;
    private String writerId;
    private String writerName;
    private String writerEmail;

    private LocalDate regDate;
    private LocalDate modDate;

    private int replyCount; // 게시물 댓글 수

    private String block = "unblock"; // 차단 여부
}
