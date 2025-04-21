package web.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "image")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageEntity extends BaseTime  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ino;

    @Column( columnDefinition = "longtext") // 파일명 또는 경로/URL 저장 고려하여 길이 설정
    private String iname; // 원본 파일명, 저장된 파일명/경로/URL 등

    // Product와의 양방향 관계 (ManyToOne)
    @ManyToOne
    @JoinColumn(name = "pno") // FK 컬럼 지정
    private ProductEntity productEntity;
}
