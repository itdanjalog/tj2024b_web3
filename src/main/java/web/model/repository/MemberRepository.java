package web.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import web.model.entity.MemberEntity;

import java.util.Optional; // null 방지를 위해 Optional 사용 권장

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Integer> {
    // JpaRepository< MemberEntity , Integer > : MemberEntity의 PK 타입이 int 이므로 Integer 사용

    // 컨트롤러 예시에서 이메일로 회원 정보를 찾는 로직이 필요했으므로 추가
    // 이메일을 기준으로 회원을 찾는 메소드 (Spring Data JPA 쿼리 메소드 규칙)
    Optional<MemberEntity> findByMemail(String memail);

    // 이메일 중복 검사를 위한 메소드 (존재 여부만 확인)
    boolean existsByMemail(String memail);

    // 다른 커스텀 쿼리 메소드 추가 가능
}