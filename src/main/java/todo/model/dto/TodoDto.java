package todo.model.dto;

import todo.model.entity.TodoEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data@NoArgsConstructor@AllArgsConstructor@Builder
public class TodoDto {
    private int id; // 할일 식별번호
    private String title; // 할일 제목
    private String content; // 할일 내용
    private boolean done; // 할일 상태
    private LocalDateTime createAt; // + 등록 날짜
    
    // dto --> entity 변환함수
    public TodoEntity toEntity(){
        return TodoEntity.builder()
                .id( this.id )
                .title( this.title )
                .content( this.content )
                .done( this.done )
                .build();
    }
}
