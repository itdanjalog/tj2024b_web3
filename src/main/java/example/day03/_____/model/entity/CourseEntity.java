package example.day03._____.model.entity;

import example.day03._____.model.dto.CourseDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity @Table(name = "day03course")
@Setter@Getter@ToString
@NoArgsConstructor // 빈생생자
@AllArgsConstructor // 전체매개변수생성자
@Builder
public class CourseEntity extends BaseTime  {

    @Id // pk
    @GeneratedValue( strategy = GenerationType.IDENTITY) // auto_increment
    private int cno; // 과정번호
    @Column
    private String cname;// 과정명

    // 양방향
    @ToString.Exclude // 순환참지 방지
    @Builder.Default // 해당 엔티티를 빌더패턴 생성할경우 초기값 사용
    @OneToMany(mappedBy = "courseEntity" , cascade = CascadeType.ALL )
    private List<StudentEntity> studentEntityList  = new ArrayList<>();

    // + toDto
    public CourseDto toDto(){
        return CourseDto.builder()
                .cno( this.cno )
                .cname( this.cname )
                .build();
    }
}
