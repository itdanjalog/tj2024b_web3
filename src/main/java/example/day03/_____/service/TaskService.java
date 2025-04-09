package example.day03._____.service;

import example.day03._____.model.dto.CourseDto;
import example.day03._____.model.dto.StudentDto;
import example.day03._____.model.entity.CourseEntity;
import example.day03._____.model.entity.StudentEntity;
import example.day03._____.model.repository.CourseEntityRepository;
import example.day03._____.model.repository.StudentEntityRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service @Transactional
@RequiredArgsConstructor
public class TaskService {

    private final CourseEntityRepository courseEntityRepository;
    private final StudentEntityRepository studentEntityRepository;

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
    // 3. 특정한 과정에 학생 등록
    public boolean saveStudent( StudentDto studentDto ){
        // 1. 학생 dto --> 학생 entity 변환
        StudentEntity studentEntity = studentDto.toEntity();
        // 2. 학생 엔티티 .save
        StudentEntity saveEntity = studentEntityRepository.save( studentEntity );
        if( saveEntity.getSno() < 1 ) return false;
        // 3. 특정한 과정 엔티티 조회 , cno 를 이용하여 과정 엔티티 찾기
        CourseEntity courseEntity = courseEntityRepository.findById( studentDto.getCno() ).orElse( null );
        if( courseEntity == null ) return false;
        // 4. 등록된 학생 엔티티의 특정한 과정 엔티티 대입 <FK대입>
        saveEntity.setCourseEntity( courseEntity ); // 단방향 멤버변수에 과정엔티티 대입하기. ( fk 대입 )
        // 5.
        return true;
    }
    // 4. 특정한 과정에 학생 전체 조회
    public List<StudentDto> findAllStudent(  int cno ){
        // 1. cno 이용하여 과정 엔티티 찾기
        CourseEntity courseEntity = courseEntityRepository.findById( cno ).orElse( null );
        if( courseEntity == null ) return null;
        // 2. 조회한 과정 엔티티 안에 참조중인 학생목록
        List< StudentEntity> studentEntityList = courseEntity.getStudentEntityList();
        // 3. dto 변환
        List< StudentDto > studentDtoList = studentEntityList.stream()
                .map( entity -> entity.toDto() )
                .collect( Collectors.toList() );
        // 4.
        return studentDtoList;
    }
}





