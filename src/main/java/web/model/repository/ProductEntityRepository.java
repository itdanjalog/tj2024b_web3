package web.model.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import web.model.entity.ImgEntity;
import web.model.entity.ProductEntity;

import java.util.List;

@Repository
public interface ProductEntityRepository
        extends JpaRepository<ProductEntity,Long> {

    // 방법1. JPA 기본적인 함수 제공
    // save , findAll , findById , delete 등

    // 방법2. 쿼리메소드 , 규칙 : 명명규칙( 카멜 )
        // findBy 필드명  : select
            // findByCno[x]  : ProductEntity 에는 cno가 존재하지 않아서 불가능
                // findByCategoryEntityCno[o]
            // findByPname[o] : ProductEntity 에는 pname 존재 해서 가능.
    List<ProductEntity> findByCategoryEntityCno( long cno );

    // 방법3. 네이티브 쿼리 , 규칙 : mysql 코드 , query문에서 매개변수 사용시 앞에 :(콜론)
        // select * from product where cno = :cno
    @Query( value = "select * from product where cno = :cno" , nativeQuery = true)
    List<ProductEntity> nativeQuery1( long cno );

    // 방법4* JPQL , 규칙 : 자바만든 sql 코드/메소드


    // 네이티브 쿼리 메소드 수정: Pageable 파라미터 제거, List 반환, countQuery 제거, ORDER BY 추가
    @Query(value = "SELECT * FROM product " +
            "WHERE (:cno IS NULL OR :cno = 0 OR cno = :cno) " +
            "AND (:keyword IS NULL OR pname LIKE %:keyword% )  " +
            "ORDER BY pno DESC",
            nativeQuery = true)
    Page<ProductEntity> findBySearchFilters( // 반환 타입 List로 변경
                                             Long cno,
                                             String keyword , Pageable pageable
                                             // Pageable 파라미터 제거됨
    );
    // WHERE (:cno IS NULL OR :cno = 0 OR cno = :cno): 조회할 데이터의 조건을 지정하는 WHERE 절의 첫 번째 부분입니다. 파라미터로 받은 :cno 값에 따라 동작이 달라집니다.
    //
    //:cno IS NULL: 만약 파라미터로 전달된 cno 값이 NULL이면 이 조건은 참(true)이 됩니다.
    //:cno = 0: 만약 파라미터로 전달된 cno 값이 0이면 이 조건은 참(true)이 됩니다.
    //cno = :cno: 만약 파라미터로 전달된 cno 값이 NULL도 아니고 0도 아니면, product 테이블의 cno 컬럼 값이 전달된 :cno 파라미터 값과 같은 행만 조건에 맞습니다.
    //OR 연결: 위 세 조건 중 하나라도 참이면 이 전체 괄호 (...) 부분은 참이 됩니다. 즉, 이 부분의 역할은 "파라미터 cno가 넘어오지 않았거나(NULL) 0이면 카테고리 필터링을 하지 않고, 0보다 큰 값이 넘어오면 해당 cno를 가진 제품만 필터링하라"는 동적 조건을 구현한 것입니다.


    // AND (:keyword IS NULL OR pname LIKE %:keyword% ): WHERE 절의 두 번째 부분이며, 앞의 카테고리 조건과 AND로 연결됩니다. 즉, 앞의 조건과 이 조건 모두 참이어야 최종 결과에 포함됩니다. 이 부분은 파라미터로 받은 :keyword 값에 따라 동작합니다.
    //
    //:keyword IS NULL: 만약 파라미터로 전달된 keyword 값이 NULL이면 이 조건은 참(true)이 됩니다.
    //pname LIKE %:keyword%: 만약 파라미터로 전달된 keyword 값이 NULL이 아니면, product 테이블의 pname 컬럼 값이 전달된 :keyword 파라미터 값을 포함하는지(LIKE) 검사합니다.
    //주의: 앞서 설명드렸듯이, LIKE %:keyword% 구문은 JPA/Hibernate의 표준 파라미터 바인딩 방식에서는 의도한 대로 동작하지 않을 가능성이 매우 높습니다. 파라미터 바인딩 시 % 문자가 값의 일부로 처리되지 않아 패턴 매칭이 실패할 수 있습니다. 이 부분은 LIKE :pattern 형태로 변경하고 서비스 단에서 "%"+keyword+"%" 패턴을 만들어 전달하는 것이 올바른 방법입니다. 코드 해석 상으로는 "키워드 포함 검색을 시도한다" 정도로 이해할 수 있습니다.
    //OR 연결: 위 두 조건 중 하나라도 참이면 이 전체 괄호 (...) 부분은 참이 됩니다. 즉, "파라미터 keyword가 넘어오지 않았거나(NULL) 비어있으면 이름 필터링을 하지 않고, 값이 넘어오면 pname에 해당 키워드가 포함된 제품만 필터링하라"는 동적 조건을 구현하려는 의도입니다. (단, LIKE 구문 문제로 실제 동작은 다를 수 있음)

    // 결론적으로, A OR B OR C 와 같은 구조에서 A, B, C 중 단 하나라도 참이면 전체 결과는 참이 됩니다. 모든 조건이 거짓일 때만 전체 결과가 거짓이 됩니다.
    //
    //이러한 OR 논리를 사용하여 해당 SQL 조건은 "cno 파라미터가 없거나(NULL) 0이면 이 조건은 항상 통과시키고(참), 특정 카테고리 번호가 주어지면 그 번호와 일치하는 행만 통과시키겠다(참 또는 거짓)"는 의도를 구현한 것입니다.

}













