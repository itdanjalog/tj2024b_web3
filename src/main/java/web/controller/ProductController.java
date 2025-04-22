package web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
// import org.springframework.security.core.Authentication; // 시큐리티 미사용으로 주석 처리
// import org.springframework.security.core.context.SecurityContextHolder; // 시큐리티 미사용으로 주석 처리
// import org.springframework.security.core.userdetails.UserDetails; // 시큐리티 미사용으로 주석 처리
import org.springframework.web.bind.annotation.*;
import web.model.dto.ProductDto;
import web.service.MemberService; // 토큰에서 회원 정보 추출을 위해 MemberService 사용 가정
import web.service.ProductService;

import java.util.List;
// JWT 관련 예외 처리나 유틸리티 클래스가 있다면 import 필요
// 예: import io.jsonwebtoken.JwtException;

@RestController // @Controller + @ResponseBody
@RequestMapping("/product") // 공통 URL 경로
@RequiredArgsConstructor
@CrossOrigin("*")
public class ProductController {

    private final ProductService productService;
    // 토큰을 처리하고 회원 정보를 가져오기 위해 MemberService 주입 가정
    private final MemberService memberService;

    // 제품 등록 처리
    @PostMapping("/register")
    // DTO는 @ModelAttribute, 토큰은 @RequestHeader로 받음
    public ResponseEntity<Boolean> registerProduct(@ModelAttribute ProductDto productDto, @RequestHeader("Authorization") String token) {

        int loginMno; // 실제 mno를 저장할 변수

        try {
            // MemberService (또는 별도의 JwtService)를 통해 토큰 검증 및 mno 추출
            // memberService에 토큰을 받아 mno를 반환하는 메소드가 있다고 가정
            // 예시: public int getMnoFromToken(String token) throws Exception;
            loginMno = memberService.info(token).getMno();

            // 만약 getMnoFromToken이 유효하지 않은 경우 특정 값(예: -1)이나 예외를 반환/발생 시킬 수 있음
            if (loginMno <= 0) { // mno가 유효하지 않은 값일 경우 (서비스 로직에 따라 다름)
                System.err.println("Invalid token or user not found for token.");
                return ResponseEntity.status(401).body(false); // 401 Unauthorized
            }

        } catch (Exception e) { // 토큰 검증/파싱 중 발생할 수 있는 예외 처리 (예: JwtException 등)
            System.err.println("Token validation failed: " + e.getMessage());
            return ResponseEntity.status(401).body(false); // 401 Unauthorized
        }

        // 서비스 호출하여 제품 등록 로직 수행 (추출된 mno 사용)
        boolean result = productService.registerProduct(productDto, loginMno);

        if (result) {
            return ResponseEntity.status(200).body(true); // 성공 시 200 OK
        } else {
            // 서비스 로직 실패는 보통 내부 문제(DB 오류, 파일 저장 실패 등)일 수 있으므로 500 또는 400 고려
            // 여기서는 이전과 같이 400 Bad Request 반환
            return ResponseEntity.status(400).body(false); // 실패 시 400 Bad Request
        }
    }

    // --- 추가적으로 필요한 API 엔드포인트들 (수정, 삭제, 조회 등) ---

    // --- 1. 전체 또는 카테고리별 제품 조회 ---
    // 기존 '/all' 경로 유지 또는 '/product'로 변경 가능 (여기선 '/all' 유지)
    @GetMapping("/all")
    public ResponseEntity<List<ProductDto>> getAllProducts(
            @RequestParam(required = false) Long cno // cno 파라미터를 선택적으로 받음
    ) {
        // Service 메소드 호출 시 cno 값 전달
        List<ProductDto> productList = productService.getAllProducts(cno);
        return ResponseEntity.status(200).body(productList); // 200 OK 와 제품 목록 반환
    }


    // --- 2. 개별 제품 조회 ---
    @GetMapping("/view")
    public ResponseEntity<ProductDto> getProduct(@RequestParam long pno) {
        ProductDto productDto = productService.getProductByPno(pno);
        if (productDto != null) {
            return ResponseEntity.status(200).body(productDto); // 200 OK 와 제품 정보 반환
        } else {
            return ResponseEntity.status(404).body(null); // 404 Not Found
        }
    }

    // --- 3. 개별 제품 삭제 ---
    @DeleteMapping("/delete")
    public ResponseEntity<Boolean> deleteProduct(
            @RequestParam long pno,
            @RequestHeader("Authorization") String token) {

        int loginMno;
        try {
            // 토큰에서 mno 추출
            loginMno = memberService.info(token).getMno();
            if (loginMno <= 0) {
                return ResponseEntity.status(401).body(false);
            }
        } catch (Exception e) {
            System.err.println("Token validation failed during delete: " + e.getMessage());
            return ResponseEntity.status(401).body(false);
        }

        // 서비스 호출하여 삭제 시도 (mno 전달하여 권한 확인)
        boolean result = productService.deleteProduct(pno, loginMno);

        if (result) {
            return ResponseEntity.ok(true); // 삭제 성공 시 200 OK
        } else {
            // 삭제 실패 원인은 다양함 (제품 없음, 권한 없음 등)
            // 서비스에서 로그를 남기므로 여기서는 일반적인 실패 응답 (예: 400 또는 403)
            // 권한 없음(403)과 제품 없음(404)을 구분하려면 서비스 반환값을 더 구체화해야 함
            // 여기서는 간단히 400 Bad Request 로 처리
            return ResponseEntity.status(400).body(false);
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
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
            }
        } catch (Exception e) {
            System.err.println("Token validation failed during image delete: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
        }

        // 서비스 호출하여 이미지 삭제 시도 (ino와 mno 전달하여 권한 확인 포함)
        boolean result = productService.deleteProductImage(ino, loginMno);

        if (result) {
            return ResponseEntity.ok(true); // 삭제 성공 시 200 OK
        } else {
            // 실패 원인: 이미지 없음, 권한 없음, 파일 삭제 실패 등
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false); // 400 Bad Request
        }
    }

    // --- 5. 개별 제품 수정 ---
    // PUT 사용 (전체 교체가 아닌 부분 수정이지만, 파일 첨부 등을 고려)
    // PATCH를 사용하는 것도 의미상 맞을 수 있음
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


}