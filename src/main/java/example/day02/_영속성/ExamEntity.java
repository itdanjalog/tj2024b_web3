package example.day02._영속성;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamEntity extends JpaRepository<ExamEntity1, Long> {

}
