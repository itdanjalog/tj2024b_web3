package web.model.entity;

import jakarta.persistence.*;

@Entity @Table(name = "member")
public class MemberEntity {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)// 기본키
    private int mno;
    private String memail;
    private String mpwd;
    private String mname;
}
