package web.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntity extends BaseTime  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long pno;

    @Column( nullable = false )
    private String ptitle;

    @Column( columnDefinition = "longtext")
    private String pcontent;

    @Column
    @ColumnDefault("0")     // int , default 0
    private int pprice;

    @Column
    @ColumnDefault("0")     // int , default 0
    private int bview;

    // Category와의 양방향 관계 (ManyToOne)
    @ManyToOne
    @JoinColumn(name = "mno") // FK 컬럼 지정
    private MemberEntity memberEntity;


    // Category와의 양방향 관계 (ManyToOne)
    @ManyToOne
    @JoinColumn(name = "cno") // FK 컬럼 지정
    private CategoryEntity categoryEntity;

    // Image와의 양방향 관계 (OneToMany)
    @OneToMany(mappedBy = "productEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private List<ImageEntity> images = new ArrayList<>();

}
