package web.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import web.model.dto.MemberDto;

@Entity @Table(name = "member")
@Data @Builder
@NoArgsConstructor @AllArgsConstructor
public class MemberEntity {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)// 기본키
    private int mno;
    private String memail;
    private String mpwd;
    private String mname;
    // entity --> dto
    public MemberDto toDto(){
        return MemberDto.builder()
                .mno( mno )
                .memail( memail )
                .mname( mname )
                .build();
    }
}
