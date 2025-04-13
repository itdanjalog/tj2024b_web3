package web.service;

import io.jsonwebtoken.Jwts;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import web.model.dto.MemberDto;
import web.model.entity.MemberEntity;
import web.model.repository.MemberEntityRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService implements UserDetailsService  {

    private final MemberEntityRepository memberEntityRepository;

    private final String SECRET_KEY = "Z7sF1kXp9qLtUw3RxNpCvHyJgAeBvYwQ";  // JWT 서명 키

    // 회원가입 처리
    public void signUp(MemberDto memberDto) {
        // 비밀번호 암호화
        String encodedPassword = new BCryptPasswordEncoder().encode(memberDto.getPassword());

        // DTO를 Entity로 변환하여 저장
        memberDto.setPassword( encodedPassword );
        MemberEntity memberEntity = memberDto.toEntity( );
        memberEntityRepository.save(memberEntity);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // username으로 회원 조회
        MemberEntity memberEntity = memberEntityRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        System.out.println( memberEntity.getUsername() );
        // Entity -> UserDetails 객체로 변환
        return User
                .builder()
                .username(memberEntity.getUsername())
                .password(memberEntity.getPassword())
                .build();
    }
    // JWT 토큰을 사용하여 로그인된 사용자 정보 반환
    public MemberDto getMyInfo(String token) {
        try {
            // 토큰 검증 및 사용자 정보 추출
            String username = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
            System.out.println( username );
            // 토큰에서 추출한 username으로 사용자 정보 조회
            MemberEntity memberEntity = memberEntityRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

            // MemberEntity를 MemberDto로 변환하여 반환
            return memberEntity.toDto();

        } catch (Exception e) {
            throw new RuntimeException("JWT 검증 오류 또는 사용자를 찾을 수 없습니다.");
        }
    }

}