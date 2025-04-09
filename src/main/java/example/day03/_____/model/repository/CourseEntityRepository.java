package example.day03._____.model.repository;

import example.day03._____.model.entity.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseEntityRepository
    extends JpaRepository<CourseEntity , Integer> {
}
