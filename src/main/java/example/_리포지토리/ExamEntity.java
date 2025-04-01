package example._리포지토리;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity@Table( name = "student1") // DB 테이블 매핑
public class ExamEntity {
    // 1. 학번  , primary key
    @Id
    private String id;
    // 2. 이름 , not null
    @Column( nullable = false )
    private String name;
    // 3. 국어점수
    @Column
    private int kor;
    // 4. 영어점수
    @Column
    private int eng;
}
