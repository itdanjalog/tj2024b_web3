package web.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import web.model.entity.CategoryEntity;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    // JpaRepository< CategoryEntity , Long > : CategoryEntity의 PK 타입이 long 이므로 Long 사용

    // 필요에 따라 커스텀 쿼리 메소드 추가 가능 (예: 카테고리 이름으로 조회 등)
    // Optional<CategoryEntity> findByCname(String cname);
}