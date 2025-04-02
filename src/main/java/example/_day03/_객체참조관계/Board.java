package example._day03._객체참조관계;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data @Builder
public class Board {
    private int 게시물번호;
    private String 게시물제목;
    private String 게시물내용;

    // 단방향
    private Category category; // fk

    @ToString.Exclude // 양방향
    @Builder.Default
    List<Reply> replyList = new ArrayList<>();
}
