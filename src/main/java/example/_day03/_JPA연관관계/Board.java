package example._day03._JPA연관관계;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data @Builder
@Entity@Table(name = "day03board")
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bno;
    private String btitle;
    private String bcontent;

    // 단방향 , fk
    @ManyToOne
    @JoinColumn(name = "cno")
    private Category category;

    // 양방향
    @ToString.Exclude
    @Builder.Default
    @OneToMany(mappedBy = "board" , cascade = CascadeType.ALL , fetch = FetchType.LAZY)
    private List<Reply> replyList = new ArrayList<>();
}


