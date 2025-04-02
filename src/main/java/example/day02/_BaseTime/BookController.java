package example.day02._BaseTime;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // HTTP 요청/응답 담당
@RequestMapping("/day02/book")
@RequiredArgsConstructor
public class BookController {
    // 1. 매핑 테스트 // 2. 매개변수 테스트 // 3. 리턴 테스트 // 4. 서비스 연동 테스트
    private  final BookService bookService;

    // 1. 등록 http://localhost:8080/day02/book
    // { "도서명" :"재미있는자바" , "저자":"유재석", "출판사" : "더조은" , "출판연도" : "2025"}
    @PostMapping
    public boolean post( @RequestBody BookEntity bookEntity ){
        System.out.println("BookController.post");
        System.out.println("bookEntity = " + bookEntity);
        return bookService.post( bookEntity );
    }
    // 2. 전체 조회 http://localhost:8080/day02/book
    @GetMapping
    public List<BookEntity> get(){
        System.out.println("BookController.get");
        return bookService.get();
    }
    // 3. 수정 http://localhost:8080/day02/book
    // { "도서번호" : "1" , "도서명" :"재미있는자바" , "저자":"유재석", "출판사" : "더조은" , "출판연도" : "2025"}
    @PutMapping
    public boolean put( @RequestBody BookEntity bookEntity ){
        System.out.println("BookController.put");
        System.out.println("bookEntity = " + bookEntity);
        return bookService.put( bookEntity );
    }
    // 4. 삭제 http://localhost:8080/day02/book?도서번호=1
    @DeleteMapping
    public boolean delete( @RequestParam int 도서번호 ){
        System.out.println("BookController.delete");
        System.out.println("도서번호 = " + 도서번호);
        return bookService.delete( 도서번호 );
    }
} // class end

