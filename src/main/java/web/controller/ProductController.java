package web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.model.dto.ProductDto;
import web.service.MemberService;
import web.service.ProductService;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ProductController {
    // *
    private final ProductService productService;
    // 1.
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
        return null;
    }

}













