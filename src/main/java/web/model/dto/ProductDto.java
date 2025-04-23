package web.model.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@ToString@Getter@Setter@Builder
@NoArgsConstructor@AllArgsConstructor
public class ProductDto {

    // * 제픔 등록할때 필요한 필드
    private String pname;           // - 제품명
    private String pcontent;        // - 제품설명
    private int pprice;             // - 제품가격
    private List<MultipartFile> files = new ArrayList<>(); // - 업로드할 제품이미지들
    private int cno; // - 제품카테고리 번호

}
