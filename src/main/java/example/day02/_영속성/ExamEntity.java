package example.day02._영속성;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table( name = "example")
@Data
public class ExamEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
}
