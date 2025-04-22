package web.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import web.model.entity.ProductEntity;

import java.util.List;

@Repository // 스프링 컨테이너에 Repository 빈(Bean)으로 등록
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    // JpaRepository< 엔티티클래스명 , PK필드의타입 >
    // 기본 CRUD 메소드 (save, findById, findAll, deleteById 등) 자동 상속

    // 필요에 따라 커스텀 쿼리 메소드 추가 가능 (예: 카테고리별 제품 조회 등)
    // List<ProductEntity> findByCategoryEntity(CategoryEntity categoryEntity);

    // 카테고리 CNO를 기준으로 제품 목록을 조회하는 메소드 추가
    // JPA 쿼리 메소드 규칙: findBy + 연결된엔티티명 + 해당엔티티의필드명
    List<ProductEntity> findByCategoryEntityCno(long cno);

}