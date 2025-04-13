package web.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import web.service.MemberService;

import javax.crypto.SecretKey;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final MemberService memberService;
    //private final String SECRET_KEY = "my-secret-key"; // 실제 환경에선 환경변수로 관리

    private final String SECRET_KEY = "Z7sF1kXp9qLtUw3RxNpCvHyJgAeBvYwQ"; // 실제 환경에선 환경변수로 관리
    private final long EXPIRATION_TIME = 86400000; // 1일

    private final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512); // 안전한 키 생성


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://localhost:54680"); // 플러터 웹 주소
        config.setAllowCredentials(true); // 쿠키/세션 허용
        config.addAllowedHeader("*"); // 모든 헤더 허용
        config.addAllowedMethod("*"); // 모든 HTTP 메서드 허용
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(cors -> cors.configurationSource(corsConfigurationSource())); // CORS 설정 추가

        http.csrf(AbstractHttpConfigurer::disable);

        // 로그인 처리 및 JWT 발급
        http.formLogin(loginForm -> loginForm
                .loginProcessingUrl("/member/login.do")
                .usernameParameter("username")
                .passwordParameter("password")
                .successHandler((request, response, authentication) -> {
                    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                    String token = Jwts.builder()
                            .setSubject(userDetails.getUsername())
                            .setIssuer("더조은앱")  // 발급자 추가
                            .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                            .signWith(secretKey)
                            .compact();
                    System.out.println("sssssssssssss");
                    response.setContentType("application/json;charset=UTF-8");
                    PrintWriter out = response.getWriter();
                    new ObjectMapper().writeValue(out, Map.of("token", token));
                })
                .failureHandler((request, response, exception) -> {
                    response.setContentType("application/json");
                    response.getWriter().println("{\"error\":\"로그인 실패: " + exception.getMessage() + "\"}");
                })
        );


        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}