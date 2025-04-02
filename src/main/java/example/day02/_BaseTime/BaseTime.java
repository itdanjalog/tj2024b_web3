package example.day02._BaseTime;

import jakarta.persistence.MappedSuperclass;
import org.springframework.cglib.core.Local;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

// - DB테이블의 레코드 생성날짜 와 수정날짜를 감지하는 엔티티
// 해당 클래스는 일반 엔티티가 아닌 상속엔티티로 사용 한다.
@MappedSuperclass
public class BaseTime {

    // 1. 엔티티/레코드 의 영속/생성 날짜/시간 자동 주입
    @CreatedDate
    private LocalDateTime 생성날짜시간; // 회원가입날짜 , 제품등록일 , 주문일

    // 2. 엔티티/레코드 의 수정 날짜/시간 자동 주입
    @LastModifiedDate
    private LocalDateTime 수정날짜시간; // 회원수정날짜 , 제품수정일 , 주문수정일

}
