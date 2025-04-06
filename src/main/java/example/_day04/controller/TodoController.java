package example._day04.controller;

import example._day04.model.dto.TodoDto;
import example._day04.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/day04/todos") // 공통 URL prefix
@RequiredArgsConstructor
@CrossOrigin("*")
public class TodoController {

    private final TodoService todoService;


    // 5. 등록
//     {
//      "title": "운동하기",
//      "content": "헬스장 가서 유산소 30분",
//      "done": false
//    }
    @PostMapping
    public TodoDto createTodo(@RequestBody TodoDto dto) {
        return todoService.createTodo(dto);
    }
    // 1. 전체 조회
    @GetMapping
    public List<TodoDto> getAllTodos() {
        return todoService.getAllTodos();
    }

    // 2. 개별 조회
    // http://localhost:8080/day04/todos/view?id=1
    @GetMapping("/view")
    public TodoDto getTodoById(@RequestParam Long id) {
        return todoService.getTodoById(id);
    }


    // 7. 수정
    // {
    //  "id": 3,
    //  "title": "운동 완료!",
    //  "content": "헬스장 다녀옴. 유산소 완료",
    //  "done": true
    //}
    @PutMapping()
    public TodoDto updateTodo(@RequestBody TodoDto dto) {
        return todoService.updateTodo(dto);
    }

    // 6. 삭제
    @DeleteMapping
    public boolean deleteTodo(@RequestParam Long id) {
        return todoService.deleteTodo(id);
    }


    // 3. 상태 여부로 필터링
    // http://localhost:8080/day04/todos/done?done=false
    @GetMapping("/done")
    public List<TodoDto> getTodosByDone(@RequestParam boolean done) {
        return todoService.getTodosByDone(done);
    }

    // 4. 제목 검색
    @GetMapping("/search")
    public List<TodoDto> searchByTitle(@RequestParam String keyword) {
        return todoService.searchByTitle(keyword);
    }

    // 페이징처리
    // http://localhost:8080/day04/todos/page?page=1&size=3
    @GetMapping("/page")
    public List<TodoDto> getTodosByPage(@RequestParam(defaultValue = "1") int page,
                                        @RequestParam(defaultValue = "3") int size) {
        return todoService.getTodosByPage(page, size);
    }

}