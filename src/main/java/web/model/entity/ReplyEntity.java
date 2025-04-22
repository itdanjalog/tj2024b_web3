package web.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity@Table(name = "reply")
@Getter @Setter @Builder @ToString @NoArgsConstructor @AllArgsConstructor // 롬복
public class ReplyEntity extends BaseTime {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private long rno; // 댓글 식별번호

    @Column( nullable = false )
    private String rcontent; // 댓글내용

    // * 단방향
    @ManyToOne@JoinColumn(name = "mno")
    private MemberEntity memberEntity; // 작성자

    @ManyToOne@JoinColumn( name = "pno" )
    private ProductEntity productEntity; //

}








