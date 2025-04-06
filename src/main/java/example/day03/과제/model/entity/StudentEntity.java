package example.day03.과제.model.entity;

import example.day03.과제.model.dto.StudentDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.*;

@Entity@Table(name = "day03student")
@Setter@Getter@ToString
@NoArgsConstructor // 빈생생자
@AllArgsConstructor // 전체매개변수생성자
@Builder
public class StudentEntity extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int sno; // 학번
    @Column
    private String sname; //학생이름

    // FK 설정 , 단방향
    @ManyToOne
    private CourseEntity courseEntity;

    // + toDto
    public StudentDto toDto(){
        return StudentDto.builder()
                .sno( this.sno )
                .sname( this.sname )
                .build();
    }
}
