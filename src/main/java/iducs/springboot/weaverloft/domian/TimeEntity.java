package iducs.springboot.weaverloft.domian;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import java.time.LocalDateTime;

/**
 * 공통적으로 사용되는 컬럼이므로, 이를 상속한 클래스에서 컬럼 추가
 */
@Getter
public class TimeEntity {
    @Column(name = "created_date")
    @CreatedDate
    private LocalDateTime createdDate;

    @Column(name = "modified_date")
    @LastModifiedDate
    private LocalDateTime modifiedDate;
}
