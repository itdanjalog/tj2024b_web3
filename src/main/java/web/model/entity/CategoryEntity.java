package web.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "category")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryEntity extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long cno;

    @Column( nullable = false, length = 100)
    private String cname;

    // Product와의 양방향 관계 (OneToMany)
    @OneToMany(mappedBy = "categoryEntity", cascade = CascadeType.ALL,  fetch = FetchType.LAZY)
    @Builder.Default // Builder 사용 시 초기화 보장
    @ToString.Exclude
    private List<ProductEntity> products = new ArrayList<>();

}
