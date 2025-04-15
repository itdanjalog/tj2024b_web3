package web.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import web.model.dto.MemberDto;
import web.model.entity.MemberEntity;
import web.model.repository.MemberEntityRepository;
import web.util.JwtUtil;

@Service // Spring MVC2 service
@RequiredArgsConstructor
@Transactional // 트랜잭션 : 여러개의 SQL 명령어 를 하나의 논리 단위 묶음
// 트랜잭션은 성공 또는 실패 , 부분 성공은 없다.
// 메소드 안에서 여러가지 SQL 실행할 경우 하나라도 오류가 발생하면 롤백(취소) * JPA 엔티티 수정 필수!
public class MemberService {

    private final MemberEntityRepository memberEntityRepository;

    private final JwtUtil jwtUtil;

    // [1] 회원가입 , BCryptPasswordEncoder 로 암호화
    public boolean signUp(  MemberDto memberDto ){
        // 1.암호화
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); // 암호화 비크립트 객체 생성
        String hashedPwd = passwordEncoder.encode( memberDto.getMpwd() ); // 암호화 지원하는 함수 .encode( 암호화할데이터 )
        memberDto.setMpwd( hashedPwd );
        // 2. DTO 를 entity 변환하기.
        MemberEntity memberEntity = memberDto.toEntity();
        // 3. 리포지토리 이용한 entity 영속화하기 , 영속된 결과 반환
        MemberEntity saveEntity = memberEntityRepository.save( memberEntity );
        // 4. 영속된 엔티티의 (자동생성된) pk 확인
        if( saveEntity.getMno() >= 1 ){ return true;}
        return false;
    }

    // [2] 로그인 , BCryptPasswordEncoder 로 복호화 하고 일치한다면 jwt 발급하기
    public String login(  MemberDto memberDto ){
        // 1. 이메일(또는 아이디)로 DB에서 사용자 정보 조회
        MemberEntity memberEntity = memberEntityRepository.findByMemail(memberDto.getMemail());

        // 2. 사용자가 존재하지 않으면 false 반환
        if (memberEntity == null) {
            return null;
        }

        // 3. 비밀번호 검증
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        boolean isMatch = passwordEncoder.matches(memberDto.getMpwd(), memberEntity.getMpwd());

        // 4. 비밀번호 일치하면 JWT 발급 (예: JwtUtil.createToken())
        if (isMatch) {
            String token = jwtUtil.createToken(memberEntity.getMemail() );
            // 여기서 토큰을 클라이언트에 전달하는 로직은 Controller 단에서 처리
            System.out.println("발급된 JWT: " + token); // 예시 출력
            return token;
        }

        return null;
    }

    // [3] 로그인된 토큰으로 로그인된 내 정보 확인
    public MemberDto info(String token) {
        // 1. 토큰 유효성 검사

        // 2. 이메일 추출
        String email = jwtUtil.validateToken(token);
        if( email == null ) return  null;
        // 3. DB에서 사용자 정보 조회
        MemberEntity memberEntity = memberEntityRepository.findByMemail(email);
        if (memberEntity == null) {
            return null;
        }
        // 4. Entity → DTO 변환 후 반환
        System.out.println("발급된 JWT 의 회원정보 : " + memberEntity.toDto() );
        return memberEntity.toDto();
    }

}
