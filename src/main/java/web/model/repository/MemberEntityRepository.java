package web.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import web.model.entity.MemberEntity;

@Repository // Spring MVC2 Repository
public interface MemberEntityRepository
    extends JpaRepository<MemberEntity,Integer> {
    // 상속 하므로써 JPA 기본적인 CRUD 제공

    MemberEntity findByMemail(String memail);

}