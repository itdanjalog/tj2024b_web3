package web.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import web.model.entity.ProductEntity;
import web.model.entity.ReplyEntity;

@Repository
public interface ReplyEntityRepository
        extends JpaRepository<ReplyEntity,Long> {
}
