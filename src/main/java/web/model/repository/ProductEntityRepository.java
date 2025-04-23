package web.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import web.model.entity.ImgEntity;
import web.model.entity.ProductEntity;

import java.util.List;

@Repository
public interface ProductEntityRepository
        extends JpaRepository<ProductEntity,Long> {

    // 방법1. JPA 기본적인 함수 제공
    // save , findAll , findById , delete 등

    // 방법2. 쿼리메소드 , 규칙 : 명명규칙( 카멜 )
        // findBy 필드명  : select
            // findByCno[x]  : ProductEntity 에는 cno가 존재하지 않아서 불가능
                // findByCategoryEntityCno[o]
            // findByPname[o] : ProductEntity 에는 pname 존재 해서 가능.
    List<ProductEntity> findByCategoryEntityCno( long cno );

    // 방법3. 네이티브 쿼리 , 규칙 : mysql 코드 , query문에서 매개변수 사용시 앞에 :(콜론)
        // select * from product where cno = :cno
    @Query( value = "select * from product where cno = :cno" , nativeQuery = true)
    List<ProductEntity> nativeQuery1( long cno );

    // 방법4* JPQL , 규칙 : 자바만든 sql 코드/메소드

}













