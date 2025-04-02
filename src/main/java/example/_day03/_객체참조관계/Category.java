package example._day03._객체참조관계;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data @Builder
public class Category {
    private int 카테고리번호;
    private String 카테고리명;

    @ToString.Exclude // 양방향
            @Builder.Default
    List<Board> boardList = new ArrayList<>();

}
