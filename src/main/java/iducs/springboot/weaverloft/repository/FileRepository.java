package iducs.springboot.weaverloft.repository;

import iducs.springboot.weaverloft.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FileRepository  extends JpaRepository<FileEntity, Long> {

    @Modifying
    @Query("delete from FileEntity f where f.board.bno =:bno ")
    void deleteByBno(@Param("bno")Long bno);

}
