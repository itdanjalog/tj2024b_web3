package web.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import web.model.entity.CategoryEntity;

@Repository
public interface CategoryEntityRepository
        extends JpaRepository<CategoryEntity,Long> {
}
