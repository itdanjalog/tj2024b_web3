package example._day04.model.dto;

import example._day04.model.entity.TodoEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder@ToString
public class TodoDto {

    private Long id;
    private String title;
    private String content;
    private Boolean done;

    private LocalDateTime createAt;


    // DTO → Entity
    public TodoEntity toEntity() {
        return TodoEntity.builder()
                .id(this.id)
                .title(this.title)
                .content(this.content)
                .done(this.done)
                .build();
    }
}