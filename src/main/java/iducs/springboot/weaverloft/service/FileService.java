package iducs.springboot.weaverloft.service;

import iducs.springboot.weaverloft.domain.BoardDTO;
import iducs.springboot.weaverloft.domain.FileDTO;
import iducs.springboot.weaverloft.entity.BoardEntity;
import iducs.springboot.weaverloft.entity.FileEntity;
import iducs.springboot.weaverloft.repository.FileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FileService {
    private FileRepository fileRepository;

    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Transactional
    public Long saveFile(FileDTO fileDTO) {
        return fileRepository.save(fileDTO.toEntity(fileDTO)).getId();
    }

    public Long updateFile(FileDTO fileDTO) {
        return fileRepository.save(fileDTO.toEntity(fileDTO)).getId();
    }

    @Transactional
    public FileDTO getFile(Long id) {
        FileEntity file = fileRepository.findById(id).get();

        FileDTO fileDTO = FileDTO.builder()
                .id(id) // 파일 번호
                .origFilename(file.getOrigFilename())
                .filename(file.getFilename())
                .filePath(file.getFilePath())
                .build();
        return fileDTO;
    }

    public FileEntity dtoToEntity(FileDTO fileDTO) {

        BoardEntity board = BoardEntity.builder().bno(fileDTO.getBno()).build();

        FileEntity file = FileEntity.builder()
                .id(fileDTO.getId())
                .origFilename(fileDTO.getOrigFilename())
                .filename(fileDTO.getFilename())
                .filePath(fileDTO.getFilePath())
                .board(board)
                .build();

        return file;
    }



}
