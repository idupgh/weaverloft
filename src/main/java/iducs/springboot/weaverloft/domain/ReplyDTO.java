package iducs.springboot.weaverloft.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data

public class ReplyDTO {
    private Long rno;

    private String text;

    private Long bno; //게시글 번호

    private String id;

    private LocalDateTime regDate, modDate;
}
