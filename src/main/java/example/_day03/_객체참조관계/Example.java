package example._day03._객체참조관계;

import java.util.ArrayList;

public class Example {
    public static void main(String[] args) {
        // 1. 카테고리 생성
        Category category = Category.builder()
                .카테고리번호(1)
                .카테고리명("공지사항")
                //.boardList(new ArrayList<>() )
                .build();

        // 2. 게시물 생성 및 카테고리 설정
        Board board = Board.builder().
                게시물번호(1).
                게시물제목("첫 번째 공지").
                게시물내용("환영합니다!")
                .category(category)
                //.replyList( new ArrayList<>())
                .build();

        // 3. 카테고리에 게시물 추가 (양방향 관계 유지)
        category.getBoardList().add(board);

        // 4. 댓글 생성 및 게시물 설정
        Reply reply = Reply.builder().댓글번호(1).댓글내용("좋은 정보 감사합니다.").board(board).build();

        // 5. 게시물에 댓글 추가 (양방향 관계 유지)
        board.getReplyList().add(reply);

        // 6. 출력 확인
        System.out.println("카테고리명: " + category.get카테고리명());
        System.out.println("카테고리의 게시물 목록: " + category.getBoardList());
        System.out.println("게시물 제목: " + board.get게시물제목());
        System.out.println("게시물의 댓글 목록: " + board.getReplyList());
        System.out.println("댓글 내용: " + reply.get댓글내용());

        //
        System.out.println( category.getBoardList() );
        System.out.println( category.getBoardList().get(0).getReplyList() );
    }
}
