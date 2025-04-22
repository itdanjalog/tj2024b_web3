package web.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity@Table(name = "img")
@Getter @Setter @Builder @ToString @NoArgsConstructor @AllArgsConstructor // 롬복
public class ImgEntity extends BaseTime {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private long ino ; // 이미지 식별번호

    @Column( nullable = false )
    private String iname; // 이미지 명

    // * 단방향
    @ManyToOne@JoinColumn(name = "pno")
    private ProductEntity productEntity;

}










