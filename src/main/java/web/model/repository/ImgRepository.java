package web.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import web.model.entity.ImgEntity;
import web.model.entity.ProductEntity; // findByProductEntity 사용 시 필요

import java.util.List; // 여러 이미지를 반환할 경우 필요

@Repository
public interface ImgRepository extends JpaRepository<ImgEntity, Long> {
    // JpaRepository< ImgEntity , Long > : ImgEntity의 PK 타입이 long 이므로 Long 사용

    // 특정 제품(ProductEntity)에 속한 모든 이미지를 찾는 메소드 (예시)
    List<ImgEntity> findByProductEntity(ProductEntity productEntity);

}