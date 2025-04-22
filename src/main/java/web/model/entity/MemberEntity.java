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

    // * 양방향 : FK 엔티티를 여러개 가지므로 List 타입으로 만든다.
    @OneToMany( mappedBy = "memberEntity" , cascade = CascadeType.ALL , fetch = FetchType.LAZY )
    @ToString.Exclude // 양방향 설계시 toString 룸복의 순환참조 방지
    @Builder.Default // 엔티티 객체 생성시 빌더메소드 사용하면 기본값
    private List<ProductEntity> productEntityList = new ArrayList<>();

    //@OneToMany( mappedBy = "FK엔티티자바필드명")
    @OneToMany( mappedBy = "memberEntity", cascade = CascadeType.ALL , fetch = FetchType.LAZY )
    @ToString.Exclude @Builder.Default
    private List<ReplyEntity> replyEntityList = new ArrayList<>();


    // entity --> dto
    public MemberDto toDto(){
        return MemberDto.builder()
                .mno( mno )
                .memail( memail )
                .mname( mname )
                .build();
    }
}
