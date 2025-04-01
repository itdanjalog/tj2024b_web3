package example._리포지토리;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExamService {

    // 1. 조작할 엔티티리포지토리의 인터페이스
    private final ExamEntityRepository examEntityRepository;

    // - 등록
    public boolean post( ExamEntity examEntity ){
        // 2. 현재 엔티티를 저장하기 .save()
        ExamEntity examEntity2
                = examEntityRepository.save( examEntity );
        // examEntity : 영속 전 객체
        // examEntity2 : 영속 된 객체 , 매핑
        return true;
    }
    // - 전체조회
    public List<ExamEntity> get(){
        // 3. 모든 엔티티를 리스트로 반환 , .findAll()
        List<ExamEntity> examEntityList
                = examEntityRepository.findAll();
        return examEntityList;
    }
    // - 수정
    public boolean put( ExamEntity examEntity ){
        // 4. 현재 엔티티의 ID가 존재하면 UPDATE / 없으면 INSERT , .save()
        examEntityRepository.save( examEntity );
        return true;
    }
    // - 수정 : 존재하는 ID만 수정 , .findById( pk값 )
    @Transactional // 아래 메소드 에서 하나라도 sql 문제발생하면 전체 취소
    public boolean put2( ExamEntity examEntity ){
        // 1. id 해당하는 엔티티 찾기
        Optional< ExamEntity > optionalExamEntity =
            examEntityRepository.findById( examEntity.getId() );
        // 2. 만약에 조회한 엔티티가 있으면 .isPresent()
        if( optionalExamEntity.isPresent() ){
            // 3. Optional 객체에서 (영속된)엔티티 꺼내기
            ExamEntity entity = optionalExamEntity.get();
            entity.setName( examEntity.getName() );
            entity.setKor( examEntity.getKor());
            entity.setEng( examEntity.getEng() );
            return true;
        }
        return false;
    }












}
