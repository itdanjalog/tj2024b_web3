package example.day02._영속성;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service@Transactional
public class ExamService {

    @PersistenceContext
    private EntityManager entityManager;

    public void test(){
        // 1️⃣ 비영속 상태 (Transient)
        ExamEntity1 member = new ExamEntity1();
        member.setName("유재석");
        System.out.println("★ 비영속 상태: " + member.getName());

        // 2️⃣ 영속 상태 (Persistent)
        entityManager.persist(member);
        System.out.println("★ 영속 상태: " + member.getName() + " (ID 할당됨) -> ID: " + member.getId());
        member.setName("유재석수정");
        entityManager.flush(); // 트랜잭션 중간에 DB반영

        // 3️⃣ 준영속 상태 (Detached)
        entityManager.detach(member);
        System.out.println("★ 준영속 상태: " + member.getName() + " (JPA 관리에서 제외됨)");
        member.setName("유재석수정수정");


        ExamEntity1 member2 = new ExamEntity1();
        member2.setName("강호동");
        entityManager.persist(member2);

        // 4️⃣ 삭제 상태 (Removed)
        entityManager.remove(member2);
        System.out.println("★ 삭제 상태: " + member2.getName() + " (삭제됨)");
    }

}
