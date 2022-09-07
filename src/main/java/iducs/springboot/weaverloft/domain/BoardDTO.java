package iducs.springboot.weaverloft.domain;

import lombok.*;

import java.time.LocalDateTime;

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

    private LocalDateTime regDate;
    private LocalDateTime modDate;

    @Builder.Default
    private String block = "unblock"; // 차단 여부

    private int views;
    private int replyCount; // 게시물 댓글 수

    private Long fileId;
}
