package web.model.dto;

import lombok.*;
import web.model.entity.MemberEntity;

@Data @Builder
@NoArgsConstructor@AllArgsConstructor
public class MemberDto {
    private int mno;
    private String memail;
    private String mpwd;
    private String mname;
    // dto --> entity
    public MemberEntity toEntity(){
        return MemberEntity.builder()
                .mno( mno )
                .memail( memail )
                .mpwd( mpwd )
                .mname( mname )
                .build();
    }
}
