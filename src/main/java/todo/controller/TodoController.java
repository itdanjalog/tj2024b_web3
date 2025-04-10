package todo.controller;

import todo.model.dto.TodoDto;
import todo.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/day04/todos")
@RequiredArgsConstructor
@CrossOrigin("*") // 플러터 dio (web사용시) 테스트 용도
public class TodoController {

    private final TodoService todoService;

    // 1. 개별 등록
    @PostMapping // http://localhost:8080/day04/todos
    // { "title" : "운동하기" , "content" : "매일10분달리기" , "done" : "false" }
    public TodoDto todoSave(@RequestBody TodoDto todoDto ){
        return todoService.todoSave( todoDto );
    }

    // 2. 전체 조회
    @GetMapping
    public List<TodoDto> todoFindAll( ){
        return todoService.todoFindAll();
    }

    // 3. 개별 조회
    @GetMapping("/view")
    // http://localhost:8080/day04/todos/view?id=1
    public TodoDto todoFindById( @RequestParam int id ){
        return todoService.todoFindById( id );
    }

    // 4. 개별 수정
    @PutMapping
    // { "id" : "1" , "title" : "운동하기22" , "content" : "매일10분달리기22" , "done" : "true" }
    public TodoDto todoUpdate( @RequestBody TodoDto todoDto ){
        return todoService.todoUpdate( todoDto );
    }

    // 5. 개별 삭제
    // http://localhost:8080/day04/todos?id=1
    @DeleteMapping
    public boolean todoDelete( @RequestParam int id ){
        return todoService.todoDelete( id );
    }

    // 6. 전체조회( + 페이징처리 )
    // http://localhost:8080/day04/todos/page?page=1&size=3
    @GetMapping("/page")
    public List<TodoDto> todoFindByPage(
            // @RequestParam( defaultValue = "초기값") : 만약에 매개변수값이 존재하지 않으면 초기값 대입
            @RequestParam( defaultValue = "1") int page , // page : 현재 조회할 페이지번호 , 초기값 = 1
            @RequestParam( defaultValue = "3") int size ){ // size : 현재 조회할 페이지당 자료 개수 , 초기값 = 3
        return todoService.todoFindByPage( page , size );
    }

    // 7. 제목 검색 조회1 ( 입력한 값이 *일치* 한 제목 조회 )
    // http://localhost:8080/day04/todos/search1?title=운동하기
    @GetMapping("/search1")
    public List<TodoDto> search1( @RequestParam String title ){
        return todoService.search1( title );
    }
    // 8. 제목 검색 조회2 ( 입력한 값이 *포함* 된 제목 조회 )
    // http://localhost:8080/day04/todos/search2?keyword=운동
    @GetMapping("/search2")
    public List<TodoDto> search2( @RequestParam String keyword ){
        return todoService.search2( keyword );
    }

} // class end









