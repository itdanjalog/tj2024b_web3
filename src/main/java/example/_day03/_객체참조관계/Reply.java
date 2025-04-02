package example._day03._객체참조관계;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class Reply {
    private int 댓글번호;
    private String 댓글내용;

    // 단방향
    private Board board; // fk
}
