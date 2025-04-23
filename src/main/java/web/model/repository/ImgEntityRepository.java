package web.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import web.model.entity.CategoryEntity;
import web.model.entity.ImgEntity;

@Repository
public interface ImgEntityRepository
        extends JpaRepository<ImgEntity,Long> {
}
