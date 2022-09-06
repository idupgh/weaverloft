package iducs.springboot.weaverloft.dto;

import iducs.springboot.weaverloft.domian.Board;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 게시글의 등록과 수정을 처리할 요청(Request) 클래스
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardRequestDto {
    private Long id;
    private String title;
    private String writer;
    private String content;
    private LocalDateTime createdDate, modifiedDate;
    private int view;

    /* Dto -> Entity */
    public Board toEntity() {
        Board board = Board.builder()
                .id(id)
                .title(title)
                .content(content)
                .writer(writer)
                .content(content)
                .view(0)
                .build();

        return board;
    }
}
