package web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import web.model.dto.MemberDto;
import web.model.entity.MemberEntity;
import web.service.MemberService;

@RestController // Spring MVC2 controller
@RequestMapping( "/member")  // 공통 url 정의
@RequiredArgsConstructor // final(수정불가) 필드의 생성자 자동 생성
@CrossOrigin("*")
// -> 관례적으로 클래스 내부에서 사용하는 모든 필드들을 수정불가능 상태로 사용한다.
public class MemberController {
    // -> 관례적으로 다른곳에 해당하는 필드를 수정못하도록 final 사용한다.(안정성 보장)
    // -> 즉 final 사용시 @RequiredArgsConstructor 떄문에 @Autowired 안해도 된다.
    private final MemberService memberService;

    // [1] 회원가입 // { "memail" : "qwe@naver.com" , "mpwd" : "qwe" , "mname" : "유재석" }
    @PostMapping("/signup")
    public boolean signUp( @RequestBody MemberDto memberDto ){
        return memberService.signUp( memberDto );
    }
} // class end








