package iducs.springboot.weaverloft.service;

import iducs.springboot.weaverloft.domain.FileDTO;
import iducs.springboot.weaverloft.entity.BoardEntity;
import iducs.springboot.weaverloft.entity.FileEntity;
import iducs.springboot.weaverloft.repository.FileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileService {
    private FileRepository fileRepository;

    public List findById(FileDTO fileDTO) {
        FileEntity file = dtoToEntity(fileDTO);
        fileRepository.findById(file.getId());
        return null;
    }


    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Transactional
    public Long saveFile(FileDTO fileDTO) {
        FileEntity file = dtoToEntity(fileDTO);

        fileRepository.save(file);

        return file.getId();
    }

    public FileDTO deleteFile(Long id, Long bno) {
        fileRepository.deleteById(id);

        return null;
    }

    public Long updateFile(FileDTO fileDTO) {
        FileEntity file = dtoToEntity(fileDTO);

        fileRepository.save(file);

        return file.getId();
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

    public FileDTO entityToDTO(FileEntity file){

        FileDTO fileDTO = FileDTO.builder()
                .id(file.getId())
                .filePath(file.getFilePath())
                .filename(file.getFilename())
                .origFilename(file.getOrigFilename())
                .build();

        return fileDTO;
    }

    public List<FileDTO> getList(Long bno) {

        List<FileEntity> result = fileRepository.getFilesByBoardOrderById(BoardEntity.builder().bno(bno).build());

        return result.stream().map(reply -> entityToDTO(reply)).collect(Collectors.toList());
    }



}
