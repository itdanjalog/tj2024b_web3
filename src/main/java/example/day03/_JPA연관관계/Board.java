package example.day03._JPA연관관계;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Data@Builder
@Entity@Table(name = "day03board")
public class Board {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private int bno; // 게시물번호
    private String btitle; // 게시물제목
    private String bcontent; // 게시물내용

    // + 단방향 , 카테고리 참조 , FK필드
    @ManyToOne // fk필드 선언 방법
    private Category category;

}









