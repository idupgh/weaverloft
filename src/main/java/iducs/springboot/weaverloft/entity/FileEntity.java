package iducs.springboot.weaverloft.entity;

import lombok.*;

import javax.persistence.*;

@Table(name="tb_file")
@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FileEntity {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String origFilename;

    @Column(nullable = false)
    private String filename;

    @Column(nullable = false)
    private String filePath;

}
