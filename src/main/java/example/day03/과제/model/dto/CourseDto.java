package example.day03.과제.model.dto;

import example.day03.과제.model.entity.CourseEntity;
import lombok.Builder;
import lombok.Data;

@Data@Builder
public class CourseDto {
    private int cno;
    private String cname;

    // + toEntity
    public CourseEntity toEntity(){
        return CourseEntity.builder()
                .cno( this.cno )
                .cname( this.cname )
                // .studentEntityList() 생략가능 , @Builder.Default
                .build();
    }

}
