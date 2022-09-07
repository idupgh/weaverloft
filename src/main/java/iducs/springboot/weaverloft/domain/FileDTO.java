package iducs.springboot.weaverloft.domain;

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

    public FileEntity toEntity() {
        FileEntity build = FileEntity.builder()
                .id(id)
                .origFilename(origFilename)
                .filename(filename)
                .filePath(filePath)
                .build();
        return build;
    }
}
