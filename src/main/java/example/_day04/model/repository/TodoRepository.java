package example._day04.model.repository;

import example._day04.model.entity.TodoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<TodoEntity, Long> {

    // [1] ✅ 쿼리 메서드 (JPQL 방식: 메서드 이름 기반으로 자동 생성) ======================================= //

    // [1-1] 제목이 일치하는 항목 조회
    List<TodoEntity> findByTitle(String title);

    // [1-2] 완료 여부로 조회
    List<TodoEntity> findByDone(boolean done);

    // [1-3] 제목 포함 여부로 조회 (LIKE 검색)
    List<TodoEntity> findByTitleContaining(String keyword);

    // [1-4] 제목 또는 내용에 포함된 항목 조회
    List<TodoEntity> findByTitleContainingOrContentContaining(String titleKeyword, String contentKeyword);

    // [1-5] 특정 제목이 존재하는지 확인
    boolean existsByTitle(String title);

    // [1-6] 미완료된 항목이 존재하는지 확인
    boolean existsByDoneFalse();

    // [1-7] 완료된 항목 개수 조회
    long countByDoneTrue();

    // [1-8] 특정 제목의 개수 조회
    long countByTitle(String title);

    // [1-9] 특정 제목의 항목 삭제
    void deleteByTitle(String title);

    // [1-10] 완료된 항목 모두 삭제
    void deleteByDoneTrue();


    // [2] ✅ 네이티브 쿼리 (SQL 직접 작성) ========================================================= //

    // [2-1] 전체 조회
    @Query(value = "SELECT * FROM todo", nativeQuery = true)
    List<TodoEntity> findAllNative();

    // [2-2] 제목이 정확히 일치하는 항목 조회  // @Param("title") 쿼리에 바인딩할 매개변수 정의한다.
    @Query(value = "SELECT * FROM todo WHERE title = :title", nativeQuery = true)
    List<TodoEntity> findByTitleNative(@Param("title") String title);

    // [2-3] 제목에 특정 키워드가 포함된 항목 조회 (LIKE 검색)
    @Query(value = "SELECT * FROM todo WHERE title LIKE %:keyword%", nativeQuery = true)
    List<TodoEntity> findByTitleContainsNative(@Param("keyword") String keyword);

    // [2-4] 제목 또는 내용에 키워드가 포함된 항목 조회
    @Query(value = "SELECT * FROM todo WHERE title LIKE %:keyword% OR content LIKE %:keyword%", nativeQuery = true)
    List<TodoEntity> findByTitleOrContentContainsNative(@Param("keyword") String keyword);

    // [2-5] 특정 제목의 항목 개수 조회
    @Query(value = "SELECT COUNT(*) FROM todo WHERE title = :title", nativeQuery = true)
    long countByTitleNative(@Param("title") String title);

    // [2-6] 특정 제목을 가진 항목 삭제
    @Modifying // @Modifying	수정/삭제/삽입 쿼리 필수	@Query는 SELECT 전용이기 때문
    @Query(value = "DELETE FROM todo WHERE title = :title", nativeQuery = true)
    int deleteByTitleNative(@Param("title") String title);

    // [2-7] 특정 ID의 제목 수정
    @Modifying
    @Query(value = "UPDATE todo SET title = :title WHERE id = :id", nativeQuery = true)
    int updateTitleById(@Param("id") Long id, @Param("title") String title);

    // [2-8] 새 Todo 항목 삽입 (DTO는 직접 사용 불가)
    @Modifying
    @Query(value = "INSERT INTO todo (title, content, done) VALUES (:title, :content, :done)", nativeQuery = true)
    int insertTodo(@Param("title") String title,
                   @Param("content") String content,
                   @Param("done") boolean done);

}


// ✅ 1. 쿼리 메서드란?
//Spring Data JPA에서는 SQL 문장을 직접 작성하지 않고, 메서드 이름만으로 자동으로 쿼리를 생성할 수 있습니다.
//메서드 명명 규칙에 따라 findBy, existsBy, countBy , deleteBy 등을 사용해 조건을 붙이면 Spring이 쿼리를 만들어 실행합니다.

// ✅ 2. 목적
//개발자가 직접 SQL을 작성하지 않아도 조건에 맞는 조회가 가능
//생산성과 유지보수 향상
//코드의 간결성

// findBy, getBy, readBy	기본 조회 시작 키워드
//예: findById, getByTitle

//And, Or	조건 연결
//예: findByTitleAndDone, findByTitleOrContent

//Is, Equals	동등 비교 (생략 가능)
//예: findByDoneIsTrue, findByTitleEquals

//Between	범위 조회
//예: findByIdBetween(1L, 10L)

//LessThan, GreaterThan	비교
//예: findByIdLessThan(10L)

//Like, Containing	부분 문자열 검색
//예: findByTitleLike("%할일%"), findByTitleContaining("할일")

//StartingWith, EndingWith	접두사/접미사 검색
//예: findByTitleStartingWith("할")

//In, NotIn	여러 값 포함 여부
//예: findByTitleIn(List<String>)

//IsNull, IsNotNull	Null 여부 조회
//예: findByContentIsNull()

//True, False	Boolean 조회
//예: findByDoneTrue(), findByDoneFalse()

//OrderBy	정렬
//예: findByDoneOrderByIdDesc()

//Top, First	상위 1개 또는 n개
//예: findTop1ByOrderByIdDesc()


// ✅ 네이티브 쿼리(Native Query)란?
// 데이터베이스 고유의 SQL 문법을 직접 작성해서 실행하는 방식입니다.

// 🎯 네이티브 쿼리의 목적
// 복잡한 Join, 서브쿼리, 집계 / 성능을 최대한 끌어올려야 할 때	 / 기존 SQL 쿼리를 JPA로 옮기는 중일 때

