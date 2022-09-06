package iducs.springboot.weaverloft.service;

import iducs.springboot.weaverloft.domian.Board;
import iducs.springboot.weaverloft.dto.BoardRequestDto;
import iducs.springboot.weaverloft.dto.BoardResponseDto;
import iducs.springboot.weaverloft.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;

    /* CREATE */
    @Transactional
    public Long write(BoardRequestDto dto) {

        Board board = dto.toEntity();

        boardRepository.save(board);

        return board.getId();
    }

    /* 게시글 리스트 조회 readOnly 속성으로 조회속도 개선 */
    @Transactional(readOnly = true)
    public List<BoardResponseDto> findAllDesc() {
        return boardRepository.findAllDesc().stream()
                .map(BoardResponseDto::new)
                .collect(Collectors.toList());
    }

    /* READ */
    @Transactional(readOnly = true)
    public BoardResponseDto findById(Long id) {
        Board entity = boardRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id=" + id));

        return new BoardResponseDto(entity);
    }

    /* UPDATE (dirty checking)*/
    @Transactional
    public Long update(Long id, BoardRequestDto requestDto) {
        Board board = boardRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id=" + id));

        board.update(requestDto.getTitle(), requestDto.getWriter(), requestDto.getContent());

        return id;
    }
}
