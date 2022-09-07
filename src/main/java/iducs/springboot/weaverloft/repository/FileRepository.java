package iducs.springboot.weaverloft.repository;

import iducs.springboot.weaverloft.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository  extends JpaRepository<FileEntity, Long> {
}
