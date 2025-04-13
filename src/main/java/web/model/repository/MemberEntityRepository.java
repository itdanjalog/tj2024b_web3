package web.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import web.model.entity.MemberEntity;

import java.util.Optional;

@Repository
public interface MemberEntityRepository
    extends JpaRepository<MemberEntity , Integer> {

    Optional<MemberEntity> findByUsername(String username);  // username으로 회원 조회


}
