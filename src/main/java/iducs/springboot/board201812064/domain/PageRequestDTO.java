package iducs.springboot.board201812064.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


@Builder // 없으면 new 를 쓴다
@AllArgsConstructor
@Data //@Getter, @Setter, @EqualsAndHash, @RequiredArgsConstructor
public class PageRequestDTO {
    private int page; // 요청하는 페이지
    private int size; // 한 페이지에 나타나는 목록 수

    private String sort; // asc, desc
    // private Sort sort; // 문자열이 아닌 sort 객체로 정렬?
    private String type; // 검색 조건 setType(), getType()
    private String keyword; // 검색어 setKeyword(), getKeyword()

    public PageRequestDTO() {
        this.page = 1;
        this.size = 5;
    }

    public Pageable getPageable(Sort sort){
        return PageRequest.of(page -1, size, sort);
    }
}
