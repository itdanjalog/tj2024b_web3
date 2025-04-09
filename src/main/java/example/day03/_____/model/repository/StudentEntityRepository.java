package example.day03._____.model.repository;

import example.day03._____.model.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentEntityRepository
    extends JpaRepository<StudentEntity , Integer> {

}
