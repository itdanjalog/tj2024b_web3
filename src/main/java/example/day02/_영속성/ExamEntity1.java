package example.day02._영속성;

import jakarta.persistence.*;
import lombok.Data;

@Entity@Table(name = "day02exam1")
@Data
public class ExamEntity1 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
}
