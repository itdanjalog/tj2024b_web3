package example.day03._자바참조관계;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data // 롬복
@Builder // 빌더
public class Board {
    private int 게시물번호;
    private String 게시물제목;
    private String 게시물내용;
    // Category 타입으로 멤버변수 선언 가능?
    private Category category; // FK , 단방향 참조
    // 양방향 참조 , 하나의 게시물이 여러개 댓글 참조한다.
    private List<Reply> replyList;
}
