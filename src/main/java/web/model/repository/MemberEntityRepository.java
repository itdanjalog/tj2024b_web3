package web.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import web.model.entity.MemberEntity;

@Repository // Spring MVC2 Repository
public interface MemberEntityRepository
    extends JpaRepository<MemberEntity,Integer> {
    // (1) 상속 하므로써 JPA 기본적인 CRUD 제공
    // (2) 개발자 정의한 쿼리메소드 : 메소드 명명 규칙
    // (3) 개발자 정의한 네이티브쿼리 : SQL 직접 작성

    // [1] 추상메소드를 이용한 memail로 엔티티 조회하는 쿼리메소드
    MemberEntity findByMemail( String memail );
}









