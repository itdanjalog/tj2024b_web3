package web.model.entity;

import jakarta.persistence.*;
import lombok.*;
import web.model.dto.MemberDto;

import java.util.ArrayList;
import java.util.List;

@Entity @Table(name = "member")
@Data @Builder
@NoArgsConstructor @AllArgsConstructor
public class MemberEntity extends BaseTime {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)// 기본키
    private int mno;
    private String memail;
    private String mpwd;
    private String mname;

    @OneToMany(mappedBy = "memberEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private List<ProductEntity> products = new ArrayList<>();


    // entity --> dto
    public MemberDto toDto(){
        return MemberDto.builder()
                .mno( mno )
                .memail( memail )
                .mname( mname )
                .build();
    }
}
