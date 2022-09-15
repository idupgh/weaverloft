package iducs.springboot.weaverloft.domain;

import iducs.springboot.weaverloft.entity.BoardEntity;
import iducs.springboot.weaverloft.entity.FileEntity;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileDTO {
    private Long id;
    private String origFilename;
    private String filename;
    private String filePath;

    private Long bno;
}
