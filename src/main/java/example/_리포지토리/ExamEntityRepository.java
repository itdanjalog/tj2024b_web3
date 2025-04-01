package example._리포지토리;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// 엔티티(테이블) 조작(DML : insert/update/delete/select) 하는 인터페이스
// 해당 인터페이스에 JpaRepository< 조작할엔티티클래스명 , 해당엔티티의ID타입 > 상속
// < > : 제네릭
@Repository // 스프링 컨테이너에 빈 등록
public interface ExamEntityRepository
        extends JpaRepository< ExamEntity , String > {

}









