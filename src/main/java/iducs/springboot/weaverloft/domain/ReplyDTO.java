package iducs.springboot.weaverloft.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data

public class ReplyDTO {
    private Long rno;

    private String text;

    private String replier;

    private Long bno; //게시글 번호

    private LocalDate regDate, modDate;
}