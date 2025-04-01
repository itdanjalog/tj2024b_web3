package example._엔티티;

import jakarta.persistence.*;

@Entity // 해당 클래스를 DB테이블과 매핑 관계 주입( 영속성 컨텍스트 저장 )
@Table( name = "exam2") // DB테이블명 정의 , 생략시 클래스명으로 정의된다.
public class ExamEntity2 {

    @Id // primary key 제약조건 정의
    // auto_increment
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private long id;

    @Column( nullable = true , unique = false  )
    private String col1;

    @Column( nullable = false , unique = true )
    private String col2;

    @Column( columnDefinition = "longtext")
    private String col3;

}
