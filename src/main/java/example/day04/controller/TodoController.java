package example.day04.controller;

import example.day04.model.dto.TodoDto;
import example.day04.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/day04/todos")
@RequiredArgsConstructor
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
    // http://localhost:8080/day04/todos/view?id=1
    @DeleteMapping
    public boolean todoDelete( @RequestParam int id ){
        return todoService.todoDelete( id );
    }

} // class end









