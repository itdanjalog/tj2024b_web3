package example._day04.service;

import example._day04.model.dto.TodoDto;
import example._day04.model.entity.TodoEntity;
import example._day04.model.repository.TodoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service@Transactional
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;


    // 5. 등록

    public TodoDto createTodo(TodoDto dto) {
        TodoEntity saved = todoRepository.save(dto.toEntity());
        return saved.toDto();
    }

    // 1. 전체조회
    public List<TodoDto> getAllTodos() {
        return todoRepository.findAll().stream()
                .map(TodoEntity::toDto)
                .collect(Collectors.toList());
    }

    // 2. 개별 조회
    public TodoDto getTodoById(Long id) {
        // 이 상태에서 .map()을 사용하면, Optional 안에 값이 있을 때만 동작하게 됩니다.
        // Java 9부터는 Optional도 스트림으로 변환할 수 있게 되었어요.
        // findById()는 Optional을 반환하므로 .map() 사용 가능
        //map()은 값이 있을 때만 동작하는 안전한 변환
        //orElse, orElseGet, orElseThrow와 함께 많이 사용
        return todoRepository.findById(id)
                .map(TodoEntity::toDto)
                .orElse( null );
    }

    // 7. 수정

    public TodoDto updateTodo( TodoDto dto) {

        return todoRepository.findById( dto.getId() )
                .map(entity -> {
                    entity.setTitle(dto.getTitle());
                    entity.setContent(dto.getContent());
                    entity.setDone(dto.getDone());
                    return todoRepository.save(entity).toDto();
                })
                .orElse( null );
    }

    // 6. 삭제
    public boolean deleteTodo(Long id) {
        if (!todoRepository.existsById(id)) {
            return false;
        }
        todoRepository.deleteById(id);
        return true;
    }

    // 3. 완료 여부로 필터링( 쿼리 메소드 )
    public List<TodoDto> getTodosByDone(boolean done) {
        return todoRepository.findByDone(done).stream()
                .map(TodoEntity::toDto)
                .collect(Collectors.toList());
    }

    // 4. 제목 검색 (네이티브 쿼리)
    public List<TodoDto> searchByTitle(String keyword) {
        return todoRepository.findByTitleNative(keyword).stream()
                .map(TodoEntity::toDto)
                .collect(Collectors.toList());
    }

    //
    public List<TodoDto> getTodosByPage(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page-1, size , Sort.by(Sort.Direction.DESC, "id") );
        return todoRepository.findAll(pageRequest)
                .map(TodoEntity::toDto)
                .getContent();  // Page → List
    }
}