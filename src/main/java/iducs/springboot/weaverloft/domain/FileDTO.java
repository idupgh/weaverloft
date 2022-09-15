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

    public FileEntity toEntity(FileDTO fileDTO) {

        BoardEntity board = BoardEntity.builder().bno(fileDTO.getBno()).build();

        FileEntity build = FileEntity.builder()
                .id(id)
                .origFilename(origFilename)
                .filename(filename)
                .filePath(filePath)
                .build();
        return build;
    }
}
