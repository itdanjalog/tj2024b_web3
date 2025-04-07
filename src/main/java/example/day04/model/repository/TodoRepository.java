package example.day04.model.repository;

import example.day04.model.entity.TodoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepository
        extends JpaRepository<TodoEntity , Integer> {
}
