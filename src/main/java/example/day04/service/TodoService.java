package example.day04.service;

import example.day04.model.dto.TodoDto;
import example.day04.model.entity.TodoEntity;
import example.day04.model.repository.TodoEntityRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TodoService {
    private final TodoEntityRepository todoRepository;
    // 1. 등록
    public TodoDto todoSave( TodoDto todoDto ){
        // 1. dto 를 entity 변환하기
        TodoEntity todoEntity = todoDto.toEntity();
        // 2. entity를 save(영속화/db레코드 매칭/등록) 한다.
        TodoEntity saveEntity = todoRepository.save( todoEntity );
        // 3. save 로 부터 반환된 엔티티(영속화)된 결과가 존재하면 
        if( saveEntity.getId() > 0 ){
            return saveEntity.toDto(); // entity를 dto로 변환하여 반환
        }else{ // 결과가 존재하지 않으면
            return null; // null 반환
        }
    } // f end
    // 2. 전체조회
    public List<TodoDto> todoFindAll( ){
        // 1. 모든 entity 조회 , findAll()
        List<TodoEntity> todoEntityList = todoRepository.findAll();
        // 2. 모든 entity 리스트 를 dto 리스트 변환하다.
        List<TodoDto> todoDtoList = new ArrayList<>(); // 2-1 : dto 리스트 생성한다.
        for( int index = 0 ; index < todoEntityList.size() ; index++ ){ // 2-2 : entity 리스트를 순회
            TodoDto todoDto = todoEntityList.get( index ).toDto(); // 2-3 : index번째 entity 를 dto로 변환
            todoDtoList.add( todoDto ); // 2-4 : dto 리스트에 저장
        } // for end
        // 3. 결과 반환
        return todoDtoList;
    } // f end
} // class end












