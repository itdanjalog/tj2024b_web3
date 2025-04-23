package web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.model.dto.ProductDto;
import web.service.MemberService;
import web.service.ProductService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ProductController {
    // *
    private final ProductService productService;
    private final MemberService memberService;
    // 1. 제품등록
    /*  제품등록 설계
        1. Post , "/product/register"
        2. '로그인회원이 등록한다'
            토큰( Authorization ) , 등록할 값들( pname, pcontent , pprice , 여러개사진들 , cno )
        3. boolean 반환
    */
    @PostMapping("/register")
    public ResponseEntity<Boolean> registerProduct(
            @RequestHeader("Authorization") String token , // - 토큰 받기
            @ModelAttribute ProductDto productDto ){ // - multipart/form(첨부파일) 받기
        System.out.println("token = " + token + ", productDto = " + productDto);
        // 1. 현재 토큰의 회원번호(작성자) 구하기.
        int loginMno;
        try {
            loginMno = memberService.info( token ).getMno() ;
        }catch ( Exception e ){
            return ResponseEntity.status( 401 ).body( false ); // 401 Unauthorized 와 false 반환
        }
        // 2. 저장할 DTO 와 회원번호를 서비스 에게 전달.
        boolean result = productService.registerProduct(productDto, loginMno);
        if( result == false ) return ResponseEntity.status( 400 ).body( false ); // 400 Bad Request 와 false 반환
        // 3. 요청 성공시 200 반환
        return ResponseEntity.status( 201 ).body( true ); // 201 (저장) 요청성공 과 true 반환
    } // f end

//    // 2. (카테고리별) 제품 전체조회 : 설계 : (카테고리조회)?cno=3  , (전체조회)?cno
//    @GetMapping("/all")
//    public ResponseEntity< List<ProductDto> > allProducts(
//            @RequestParam( required = false) Long cno){ // required = false : cno 는 필수는 아니다 뜻.
//        List<ProductDto> productDtoList = productService.allProducts( cno );
//        return ResponseEntity.status( 200 ).body( productDtoList ); // 200 성공 과 값 반환
//    }


    // 2. 전체 또는 카테고리별 + 검색어별 제품 조회
    @GetMapping("/all")
    public ResponseEntity<List<ProductDto>> allProducts(
            @RequestParam(required = false) Long cno,       // 카테고리 번호 (선택)
            @RequestParam(required = false) String keyword  // 검색어 (선택)
    ) {
        // Service 메소드 호출 시 cno 와 keyword 전달
        List<ProductDto> productDtoList = productService.allProducts(cno, keyword);
        return ResponseEntity.ok(productDtoList); // 200 OK 와 제품 목록 반환
    }


    // 3. 제품 개별조회 : 설계 : ?pno=1
    @GetMapping("/view")
    public ResponseEntity<ProductDto> viewProduct( @RequestParam long pno ){ // required 생략시 pno 필수
        ProductDto productDto = productService.viewProduct( pno );
        if( productDto == null ) {
            return ResponseEntity.status( 404 ).body( null ); // 404 not found 와 null 반환
        }else{
            return ResponseEntity.status( 200 ).body( productDto ); // 200 과 값 반환
        }
    }

    // 4. 제품 개별삭제 : 설계 : 토큰 , 삭제할제품번호
    @DeleteMapping("/delete")
    public ResponseEntity<Boolean> deleteProduct(
        @RequestHeader("Authorization") String token,
        @RequestParam int pno ){
        // 1. 권한 확인
        int loginMno;
        try { loginMno = memberService.info(token).getMno();
        } catch (Exception e) {
            return ResponseEntity.status( 401 ).body( false );
        }
        // 2.
        boolean result = productService.deleteProduct( pno , loginMno );
        // 3.
        if( result == false ) return ResponseEntity.status( 400 ).body( false );
        // 4.
        return ResponseEntity.status( 200 ).body( true );
    }
    // --- 5. 개별 제품 수정 ---
    @PutMapping("/update") // URL 예: PUT /product?pno=3
    public ResponseEntity<Boolean> updateProduct(
            @ModelAttribute ProductDto productDto, // 수정할 내용( pno, pname, pcontent, pprice , cno ) 및 추가할 파일(files)
            @RequestHeader("Authorization") String token) {

        int loginMno;
        try {
            // 토큰에서 mno 추출
            loginMno = memberService.info(token).getMno();
            if (loginMno <= 0) {
                return ResponseEntity.status(401).body(false);
            }
        } catch (Exception e) {
            System.err.println("Token validation failed during update: " + e.getMessage());
            return ResponseEntity.status(401).body(false);
        }

        // 서비스 호출하여 제품 수정 시도
        // @ModelAttribute ProductDto에서 추가할 파일 목록(getFiles())을 가져와 전달
        boolean result = productService.updateProduct(productDto , loginMno);

        if (result) {
            return ResponseEntity.ok(true); // 수정 성공 시 200 OK
        } else {
            // 실패 원인: 제품 없음, 권한 없음, 파일 업로드 실패 등
            return ResponseEntity.status(400).body(false); // 400 Bad Request
        }
    }

    // --- 4. 개별 이미지 삭제 ---
    @DeleteMapping("/image") // URL 예: /product/image?ino=12
    public ResponseEntity<Boolean> deleteProductImage(
            @RequestParam long ino, // 삭제할 이미지의 ID (ino)
            @RequestHeader("Authorization") String token) {

        int loginMno;
        try {
            // 토큰에서 mno 추출
            loginMno = memberService.info(token).getMno();
            if (loginMno <= 0) {
                return ResponseEntity.status(401).body(false);
            }
        } catch (Exception e) {
            System.err.println("Token validation failed during image delete: " + e.getMessage());
            return ResponseEntity.status(401).body(false);
        }

        // 서비스 호출하여 이미지 삭제 시도 (ino와 mno 전달하여 권한 확인 포함)
        boolean result = productService.deleteProductImage(ino, loginMno);

        if (result) {
            return ResponseEntity.ok(true); // 삭제 성공 시 200 OK
        } else {
            // 실패 원인: 이미지 없음, 권한 없음, 파일 삭제 실패 등
            return ResponseEntity.status(400).body(false); // 400 Bad Request
        }
    }

    // 전체 카테고리 Map 조회 API
    @GetMapping("/category") // 예시 경로: /category/allmap
    public ResponseEntity<Map<Long, String>> getAllCategory() {
        // 1. CategoryService를 호출하여 Map 데이터를 가져옵니다.
        Map<Long, String> categoryMap = productService.getAllCategory();
        // 2. ResponseEntity에 Map 데이터와 200 OK 상태 코드를 담아 반환합니다.
        return ResponseEntity.ok(categoryMap);
    }

}















