package example._엔티티;

import jakarta.persistence.*;
import org.hibernate.annotations.DialectOverride;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity // 해당 클래스는 DB테이블 매핑
@Table( name = "task1todo") // db테이블 정의
public class Task1 {

    @Id // primary key
    @GeneratedValue( strategy = GenerationType.IDENTITY)// auto_increment
    private  int id;

    @Column( nullable = false , length = 100 ) // not null , varchar(100)
    private String title;

    @Column( nullable = false )
    private boolean state ;

    @Column( nullable = false )
    private LocalDate createat;

    @Column( nullable = false )
    private LocalDateTime updateat;
}
