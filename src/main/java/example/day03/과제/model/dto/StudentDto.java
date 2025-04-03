package example.day03.과제.model.dto;

import example.day03.과제.model.entity.StudentEntity;
import lombok.Builder;
import lombok.Data;

@Data @Builder
public class StudentDto {
    private int sno;
    private String sname;

    // + toEntity
    public StudentEntity toEntity(){
        return StudentEntity.builder()
                .sno( this.sno )
                .sname( this.sname )
                .build();
    }

}
