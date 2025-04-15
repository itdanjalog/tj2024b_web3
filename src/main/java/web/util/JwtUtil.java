package web.util;

import java.security.Key;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

@Component
// @Component: 이건 Spring Framework에서 사용되는 어노테이션입니다.
// 이 어노테이션이 붙은 클래스는 Spring에서 자동으로 객체로 관리된다는 뜻이에요.
// 그래서 이 클래스를 다른 곳에서 자동으로 사용할 수 있게 됩니다.
public class JwtUtil {

    private Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256) ; // 보안을 위해 외부 파일에서 주입 권장
    // SECRET_KEY 는 우리가 JWT 토큰을 만들 때 사용되는 비밀키예요. 이 키는 우리가 만든 토큰을 암호화하거나 검증할 때 필요해요.
    // HS256은 256비트 길이의 키를 사용한다는 뜻이에요. (간단히 말하면 256자 길이의 키를 쓴다는 거예요)

    // SECRET_KEY: JWT를 만들 때 사용되는 비밀키입니다.
    // 이 키를 이용해서 토큰을 암호화하고, 나만 알 수 있는 정보로 만든 뒤,
    // 나중에 이 토큰을 검증할 때 사용해요. 보통 외부 파일에서 불러오는 것이 더 안전합니다.
    // HS256: 256비트 키 (32바이트) 사용. HS256: 32~64자 (256비트)
    //HS512: 512비트 키 (64바이트) 사용. HS512: 64~128자 (512비트) qP9sLxV3tRzWn8vMbKjUyHdGcTfEeXcZwAoLpNjMqRsTuVyBxCmZkYhGjFlDnEpQzFgXt9pMwX8Sx7CtQ5VtBvKmA2QwE3D

    // 1.발급
    public  String createToken(String email) {
        String encodedKey = Base64.getEncoder().encodeToString(SECRET_KEY.getEncoded());
        System.out.println( ">>비밀키 : " + encodedKey );
        // 비밀키를 Base64로 변환해서 출력하는 코드예요. Base64는 데이터를 안전하게 표현하는 방식이에요. 그걸 출력하면 비밀키가 어떻게 생겼는지 알 수 있어요.

        return Jwts.builder()
                .setSubject(email)  // 토큰에 이메일을 담아요.
                .setIssuedAt(new Date()) //  토큰이 언제 만들어졌는지 저장해요.
                .setExpiration(new Date(System.currentTimeMillis() + (1000*60*60) ) ) // 토큰이 1시간 후에 만료되게 설정해요.
                .signWith( SECRET_KEY ) // 이 비밀키로 토큰을 암호화해요.
                .compact(); // 최종적으로 JWT 토큰을 만들고 반환해요.
    }
    // 2. JWT 유효성 검증 및 이메일 추출
    public  String validateToken(String token) {
        try {
            Claims claims = Jwts.parser() // JWT 토큰을 해석하기 위한 함수.. (클레임스)
                    .setSigningKey(SECRET_KEY) // 이 부분은, 우리가 사용하는 비밀키(SECRET_KEY)가 올바른지 검증하기 위해 설정하는 단계입니다.
                    .build() // 검증이 완료된 토큰 객체를 생성하는 역할을 합니다.
                    .parseClaimsJws(token) //  그 토큰을 해석하고, 서명 검증을 포함한 모든 검증 절차를 거친 후, 실제 JWT의 클레임 정보를 가져옵니다.
                    .getBody(); // 그 안에 있는 **본문(body)**을 가져오는 거예요.
            // 토큰이 유효하면 이메일 반환
            return claims.getSubject(); // 토큰에 담긴 이메일을 가져오는 거예요.
        } catch (ExpiredJwtException e) { // 토큰이 만료되었을 때 예외를 처리해요.
            System.out.println("❌ JWT 만료: " + e.getMessage());
        } catch (JwtException e) { // 다른 JWT 오류를 처리해요.
            System.out.println("❌ JWT 오류: " + e.getMessage());
        }
        return null; // 유효하지 않은 토큰인 경우 null 반환
    }

}
