package example.day03.과제.service;

import example.day03.과제.model.dto.CourseDto;
import example.day03.과제.model.entity.CourseEntity;
import example.day03.과제.model.repository.CourseEntityRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.stream.Collectors;

@Service @Transactional
@RequiredArgsConstructor
public class TaskService {
    private final CourseEntityRepository courseEntityRepository;
    // 1.  과정 등록
    public boolean saveCourse( CourseDto courseDto ){
        System.out.println("TaskService.saveCourse");
        System.out.println("courseDto = " + courseDto);
        // 1. DTO --> entity 변환
        CourseEntity courseEntity = courseDto.toEntity();
        // 2. 해당 entity 를 .save 하기
        CourseEntity saveEntity 
                = courseEntityRepository.save( courseEntity ); // 반환값 : 영속된 객체
        // 3. 결과 확인
        if( saveEntity.getCno() > 0 ){ return true; } // 만약에 영속 결과 cno(과정번호) 존재하면 성공
        return false; // 아니면 실패
    }
    
    // 2. 과정 전체조회
    public List<CourseDto> findAll(){
        // 1. 모든 과정를 조회한다. , findAll() 에서 엔티티 조회할경우 기본생성자를 사용한다.
        List<CourseEntity> courseEntityList = courseEntityRepository.findAll();
        // 2. 모든 과정의 엔티티를 dto로 변환한다.
        List< CourseDto > courseDtoList = courseEntityList.stream()
                                        .map( entity -> entity.toDto() )
                                        .collect( Collectors.toList() );
        // 3.
        return courseDtoList;
    }
    
}





