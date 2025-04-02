package example.day01._리포지토리;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/day01/jpa")
@RequiredArgsConstructor
public class ExamController {
    private final ExamService examService;
    // 1. C : 등록
    @PostMapping // http://localhost:8080/day01/jpa // body :{ "id": "1",  "name": "유재석",  "kor": "95","eng": "100" }
    public boolean post( @RequestBody ExamEntity examEntity ){
        // (영속 전 / 매핑 전) examEntity
        boolean result = examService.post( examEntity );
        return result;
    }
    // 2. R : 전체 조회
    @GetMapping
    public List< ExamEntity > get( ){
        return examService.get();
    }
    // 3. U : 수정
    @PutMapping
    public boolean put( @RequestBody ExamEntity examEntity ){
        boolean result = examService.put2( examEntity );
        return result;
    }

    // 4. D : 삭제
    @DeleteMapping // http://localhost:8080/day01/jpa?id=1
    public boolean delete( @RequestParam String id ){
        boolean result =  examService.delete( id );
        return result;
    }


}











