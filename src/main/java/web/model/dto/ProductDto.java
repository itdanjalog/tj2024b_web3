package web.model.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Data@NoArgsConstructor@AllArgsConstructor@Builder
public class ProductDto {

    private int cno;     // (memberEntity) 회원 번호
    private String cname; //  (memberEntity) 회원 이메일

    private long pno;
    private String ptitle;
    private String pcontent;
    private int pprice;
    private int bview;

    private int mno;     // (memberEntity) 회원 번호
    private String memail; //  (memberEntity) 회원 이메일

    // 1. 출력용 게시물 이미지 필드 ( 왜?? 파일이름만 여러개 출력하면 되니까    SPRING --> JS )
    private List< String > imageList = new ArrayList<>();
    // 2. 등록용 게시물 이미지 필드( 왜??  JS -- multipart/Form(바이트) ---> SPRING )
    private List<MultipartFile> uploadList = new ArrayList<>();

}