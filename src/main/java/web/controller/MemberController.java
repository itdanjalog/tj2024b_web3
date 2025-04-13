package web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.model.dto.MemberDto;
import web.service.MemberService;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup.do")
    public ResponseEntity<?> signUp(@RequestBody MemberDto memberDto) {
        try {
            // 비즈니스 로직으로 회원가입 처리 (DB에 저장 등)
            memberService.signUp( memberDto );
            return ResponseEntity.ok().body("회원가입 성공");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("회원가입 실패");
        }
    }
    @GetMapping("/myinfo")
    public ResponseEntity<MemberDto> getMyInfo(@RequestHeader("Authorization") String authorizationHeader) {
        // Authorization 헤더에서 JWT 토큰 추출
        String token = authorizationHeader.replace("Bearer ", "");

        try {
            // JWT 토큰을 통해 인증된 사용자 정보 가져오기
            MemberDto memberDto = memberService.getMyInfo(token);
            return ResponseEntity.ok(memberDto);
        } catch (Exception e) {
            return ResponseEntity.status(401).body(null); // 인증 실패 시 401 반환
        }
    }
}