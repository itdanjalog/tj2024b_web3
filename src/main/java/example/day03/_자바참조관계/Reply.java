package example.day03._자바참조관계;

import lombok.Builder;
import lombok.Data;

@Data@Builder
public class Reply {
    private int 댓글번호;
    private String 댓글내용;
    // Board 타입으로 멤버변수 선언 가능?
    private Board board; // FK , 단방향 참조
}
