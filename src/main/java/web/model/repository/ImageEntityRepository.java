package web.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import web.model.entity.ImageEntity;
@Repository // Spring MVC2 Repository
public interface ImageEntityRepository  extends JpaRepository<ImageEntity,Long> {
}
