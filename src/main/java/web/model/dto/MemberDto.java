package web.model.dto;

import lombok.*;
import web.model.entity.MemberEntity;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {

    private Long id;
    private String username;  // 이메일 (사용자명)
    private String password;
    private String name;      // 사용자 이름

    // MemberDto를 MemberEntity로 변환하는 메서드
    // MemberEntity를 생성하는 메서드 (MemberDto -> Entity 변환)
    public  MemberEntity toEntity() {
        return MemberEntity.builder()
                .username( this.username )
                .password( this.password )  // 암호화된 비밀번호
                .name( this.name )
                .build();
    }
}
