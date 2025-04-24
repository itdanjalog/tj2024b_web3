package web.model.dto;


import lombok.*;
import web.model.entity.CategoryEntity;

@Getter@Setter@ToString@Builder
@NoArgsConstructor@AllArgsConstructor
public class CategoryDto {
    private long cno;
    private String cname;

    // * toEntity  : 주로 저장 용도

    // * toDto ; 주로 조회 용도
    public static CategoryDto
        toDto( CategoryEntity categoryEntity ){
        return CategoryDto.builder()
                .cno( categoryEntity.getCno()  )
                .cname( categoryEntity.getCname() )
                .build();
    }
}








