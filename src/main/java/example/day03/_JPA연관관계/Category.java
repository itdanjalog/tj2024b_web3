package example.day03._JPA연관관계;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Data@Builder // 롬복
@Entity // 해당 클래스는 데이터베이스 와 영속관계로 사용
@Table(name = "day03category") // DB테이블명 정의
public class Category {
    @Id // primary key
    @GeneratedValue( strategy = GenerationType.IDENTITY ) // auto_increment
    private int cno; // 카테고리번호
    private String cname; // 카테고리명

}
