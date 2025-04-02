package example.day02._BaseTime;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Service // 비지니스로직 담당
@Transactional // 해당 클래스 메소드들이 SQL를 사용할경우 트랜잭션 적용
@RequiredArgsConstructor
public class BookService {

    private final BookEntityRepository bookEntityRepository;

    @PostMapping
    public boolean post(  BookEntity bookEntity ){
        // 1. JPA 이용한 영속성/DB테이블(레코드) 넣기
        bookEntityRepository.save( bookEntity );
        return true;
    }
    // 2. 전체 조회
    public List<BookEntity> get(){
        // 2. JPA 이용한 영속된/DB테이블(레코드) 모두 조회
        return bookEntityRepository.findAll();
    }
    // 3. 수정
    public boolean put(  BookEntity bookEntity ){
        // 3. JPA 이용한 영속된/DB테이블(레코드) 조회
        BookEntity entity = bookEntityRepository.findById( bookEntity.get도서번호() ).orElse( null );
        if( entity == null ) return false;
        // 조회된 영속객체를 수정
        entity.set도서명( bookEntity.get도서명() );
        entity.set저자( bookEntity.get저자() );
        entity.set출판연도( bookEntity.get출판연도() );
        entity.set출판사( bookEntity.get출판사() );
        return true;
    }
    // 4. 삭제
    public boolean delete(  int 도서번호 ){
        // 4. JPA 이용한 영속된/DB테이블(레코드) 삭제
        bookEntityRepository.deleteById( 도서번호 );
        return true;
    }
}
