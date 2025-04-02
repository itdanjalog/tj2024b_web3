package example._day03._JPA연관관계;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data @Builder
@Entity@Table(name = "day03category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cno;
    private String cname;

    @ToString.Exclude // 순환참조 방지
    @Builder.Default
    @OneToMany(mappedBy = "category" , cascade = CascadeType.ALL , fetch = FetchType.LAZY)
    private List<Board> boardList = new ArrayList<>();

}
