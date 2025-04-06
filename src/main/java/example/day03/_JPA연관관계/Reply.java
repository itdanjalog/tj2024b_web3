package example.day03._JPA연관관계;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Data@Builder
@Entity@Table( name = "day03reply")
public class Reply {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private int rno; // 댓글번호
    private String rcontent; // 댓글내용

    // + 단방향 , 게시물 참조 , FK 필드
    @ManyToOne // fk필드 선언 방법
    private Board board;

}
