package example._day04.model.entity;


import example._day04.model.dto.TodoDto;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "todo")
@NoArgsConstructor@AllArgsConstructor
@Getter@Setter@ToString@Builder
public class TodoEntity extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    private Boolean done;


    // Entity → DTO
    public TodoDto toDto() {
        return TodoDto.builder()
                .id(this.getId())
                .title(this.getTitle())
                .content(this.getContent())
                .done(this.getDone())
                .createAt( this.getCreateAt() )
                .build();
    }

}
