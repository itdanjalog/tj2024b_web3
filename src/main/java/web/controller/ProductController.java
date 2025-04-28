package web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.model.dto.CategoryDto;
import web.model.dto.ProductDto;
import web.model.entity.ProductEntity;
import web.service.MemberService;
import web.service.ProductService;

import java.util.List;

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
//            @RequestParam( required = false) long cno){ // required = false : cno 는 필수는 아니다 뜻.
//        List<ProductDto> productDtoList = productService.allProducts( cno );
//        return ResponseEntity.status( 200 ).body( productDtoList ); // 200 성공 과 값 반환
//    }

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

    // 5. 제품 수정 ( + 이미지 추가 )
    /*  매핑 : Put , /product/update , boolean
    *   매개변수 : 수정할값:( pname , pcontent , pprice , cno , files ) , 수정할대상:pno , 권한(token)
    * */
    @PutMapping("/update")
    public ResponseEntity<Boolean> updateProduct(
            @RequestHeader("Authorization") String token ,
            @ModelAttribute ProductDto productDto ){
        // 1. 토큰의 mno 추출
        int loginMno ;
        try{  loginMno = memberService.info( token ).getMno();
        }catch (Exception e ){ return ResponseEntity.status( 401 ).body( false ); }
        // 2. 수정 서비스 호출
        boolean result =  productService.updateProduct( productDto , loginMno );
        if( result == false ) return ResponseEntity.status( 400 ).body( false );
        return ResponseEntity.status( 200 ).body( true  );
    }

    // 6. 이미지 개별 삭제
    /*  매핑 : Delete , /product/image , boolean
        매개변수 : 삭제할대상:ino , 권한(token)
     */
    @DeleteMapping("/image")
    public ResponseEntity<Boolean> deleteImage(
            @RequestParam long ino , @RequestHeader("Authorization") String token ){
        int loginMno;
        try{ loginMno = memberService.info( token ).getMno();
        }catch (Exception e ){ return ResponseEntity.status( 401 ).body(false); }
        boolean result = productService.deleteImage( ino , loginMno );
        if( result == false ) ResponseEntity.status( 400).body( false );
        return ResponseEntity.status( 200 ).body( true  );
    }
    // 7. 카테고리 조회
    /* 매핑 : Get , /product/category , List<CategoryDto>
        매개변수 : x
     */
    @GetMapping("/category")
    public ResponseEntity<List<CategoryDto> > allCategory(){
        List< CategoryDto > categoryDtoList = productService.allCategory();
        return ResponseEntity.status( 200 ).body( categoryDtoList );
    }


    // 2. 검색+페이징처리 , 위에서 작업한 2번 메소드 주석처리 후 진행. ( + 웹/앱 : 무한스크롤 )
    /*  매핑 : Get , /product/all , List<ProductDto> ---> Page<ProductDto>
        매개변수 : cno(없으면전체조회) , page(현재페이지번호없으면1페이지) , keyword(없으면전체조회)
    */
    @GetMapping("/all")
    // public ResponseEntity< List<ProductDto> > allProducts(
    public ResponseEntity<Page<ProductDto>> allProducts(
            @RequestParam( required = false ) Long cno , //  cno : 카테고리 번호 , long(기본타입)  Long(참조타입)
            @RequestParam( defaultValue = "1" ) int page , // page : 조회할 현재페이지 번호 , defaultValue="기본값"
            @RequestParam( defaultValue = "5") int size ,  // size : 페이지당 게시물수
            @RequestParam( required = false ) String keyword ){  // keyword : (제품명) 검색어
        // List<ProductDto> productDtoList = productService.allProducts( cno , page, size , keyword );
        Page<ProductDto> productDtoList = productService.allProducts( cno , page, size , keyword );
        return ResponseEntity.status( 200 ).body( productDtoList );
    }
}













