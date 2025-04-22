package web.model.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import web.model.entity.CategoryEntity;
import web.model.entity.ImgEntity;
import web.model.entity.MemberEntity;
import web.model.entity.ProductEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter @Setter @Builder @ToString
@NoArgsConstructor @AllArgsConstructor
public class ProductDto {

    private String pname; // 제품명
    private int pprice; // 제품가격
    private String pcontent; // 제품설명
    private long cno; // 등록할 카테고리 번호
    // + 등록 시에는 이미지 파일 자체를 받음
    private List<MultipartFile> files = new ArrayList<>();

    // 참고: 다른 용도(조회 등)를 위한 필드나 Entity -> Dto 변환 메소드도 추가될 수 있음
    private long pno;
    private int pview;
    private String memail; // 작성자 이메일 (조회 시 필요할 수 있음)
    private String cname; // 카테고리 이름 (조회 시 필요할 수 있음)
    private List<String> imgList = new ArrayList<>(); // 이미지 파일명 목록 (조회 시 필요할 수 있음)

    // DTO -> Entity 변환 메소드 (Product 등록 시 사용)
    // 누가(MemberEntity) 어떤 카테고리(CategoryEntity)에 제품(pname, pcontent, pprice)을 등록했는지 정보 필요
    public ProductEntity toProductEntity(MemberEntity memberEntity, CategoryEntity categoryEntity) {
        return ProductEntity.builder()
                .pname(this.pname)
                .pprice(this.pprice)
                .pcontent(this.pcontent)
                .memberEntity(memberEntity) // 작성자 엔티티
                .categoryEntity(categoryEntity) // 카테고리 엔티티
                // ProductEntity 생성 시 pview 는 기본값 0으로 설정됨 (@ColumnDefault("0"))
                // ProductEntity 생성 시 imgEntityList 는 기본값 new ArrayList<>() 로 설정됨 (@Builder.Default)
                .build();
    }

    // Entity -> Dto 변환 메소드 (Product 조회 시 사용 예시)
    public static ProductDto fromProductEntity(ProductEntity productEntity) {
        return ProductDto.builder()
                .pno(productEntity.getPno())
                .pname(productEntity.getPname())
                .pprice(productEntity.getPprice())
                .pcontent(productEntity.getPcontent())
                .pview(productEntity.getPview())
                .cno(productEntity.getCategoryEntity().getCno())
                .cname(productEntity.getCategoryEntity().getCname())
                .memail(productEntity.getMemberEntity().getMemail()) // MemberEntity에서 이메일 가져오기
                .imgList(productEntity.getImgEntityList().stream() // 이미지 엔티티 리스트에서 이름만 추출
                        .map(ImgEntity::getIname)
                        .collect(Collectors.toList()))
                .build();
    }


}