package web.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import web.model.entity.ImgEntity;
import web.model.entity.ProductEntity;

@Repository
public interface ProductEntityRepository
        extends JpaRepository<ProductEntity,Long> {
}
