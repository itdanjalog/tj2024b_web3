package web.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import web.model.dto.MemberDto;

@Entity@Table(name = "member")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 회원 ID (자동 증가)

    private String username;  // 이메일 (사용자명)
    private String password;  // 비밀번호
    private String name;      // 사용자 이름

    // MemberEntity를 MemberDto로 변환하는 메서드
    public MemberDto toDto() {
        return MemberDto.builder()
                .id(this.id)
                .username(this.username)
                .name(this.name)
                .build();
    }

}
