package web.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.multipart.MultipartFile;
import web.model.dto.ProductDto;
import web.model.entity.CategoryEntity;
import web.model.entity.ImgEntity;
import web.model.entity.MemberEntity;
import web.model.entity.ProductEntity;
import web.model.repository.CategoryEntityRepository;
import web.model.repository.ImgEntityRepository;
import web.model.repository.MemberEntityRepository;
import web.model.repository.ProductEntityRepository;
import web.util.FileUtil;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {
    // *
    private final ProductEntityRepository productEntityRepository;
    private final MemberEntityRepository memberEntityRepository;
    private final CategoryEntityRepository categoryEntityRepository;
    private final FileUtil fileUtil;
    private final ImgEntityRepository imgEntityRepository;

    // 1.제품등록
    public boolean registerProduct( ProductDto productDto , int loginMno ){
        // 1. 현재 회원번호의 엔티티 찾기 ( 연관관계 ) FK , Optional : null 값 제어 기능 제공
        Optional<MemberEntity > optionalMemberEntity = memberEntityRepository.findById( loginMno );
        if( optionalMemberEntity.isEmpty() ) return false;  // 만약에 조회된 회원엔티티가 없으면 false
        // 2. 현재 카테고리번호의 엔티티 찾기 ( 연관관계 ) FK
        Optional<CategoryEntity> optionalCategoryEntity = categoryEntityRepository.findById( productDto.getCno() );
        if( optionalCategoryEntity.isEmpty() ) return false; // 만야겡 조회된 카테고리엔티티가 없으면 false
        // 3. ProductDto 를 ProductEntity 변환.
        ProductEntity productEntity = productDto.toEntity();
        // 4. * 단방향 관계 (FK) 주입 , cno[x] --> CategoryEntity *
        productEntity.setMemberEntity( optionalMemberEntity.get() );
        productEntity.setCategoryEntity( optionalCategoryEntity.get() );
        // 5. 영속성 연결
        ProductEntity saveEntity = productEntityRepository.save( productEntity );
        if( saveEntity.getPno() <= 0 ) return  false; // 제품번호가 0 이하 이면 실패
        // 6. 파일 처리 , 첨부파일이 비어있지 않으면 업로드 진행
        if( productDto.getFiles() != null && !productDto.getFiles().isEmpty() ){
            // 6-1 : 여러개 첨부파일 이므로 반복문활용
            for (MultipartFile file : productDto.getFiles() ){
                // 6-2 : FileUtil 에서 업로드 메소드 호출 ( web2 에서 만든 함수들 )
                String saveFileName = fileUtil.fileUpload( file );
                // 6-3 : * 만약에 업로드 실패하면 트랜잭션 롤백 *  @Transactional
                if( saveFileName == null ){ // 6-4 : 강제 예외 발생 해서 트랜잭션 롤백하기.
                    throw new RuntimeException("업로드 중에 오류 발생");
                }
                // 6-4 : 업로드 성공했으면 ImgEntity 만들기 , 업로드한 파일명 넣기
                ImgEntity imgEntity = ImgEntity.builder().iname( saveFileName ).build();
                // 6-5 : * 단방향 관계 (FK) 주입 , pno[x] --> productEntity *
                imgEntity.setProductEntity( saveEntity );
                // 6-6 : ImgEntity 영속화
                imgEntityRepository.save( imgEntity );
            }
        }
        // 7. 성공 반환
        return true;
    } // class end

    // 2. (카테고리별) 제품 전체조회 : 설계 : (카테고리조회)?cno=3  , (전체조회)?cno
    public List<ProductDto> allProducts( long cno ){
        // 1. 조회된 결과를 저장하는 리스트 변수
        List<ProductEntity> productEntityList;
        // 2. cno 에 따라 카테고리별 조회 vs 전체조회
        if( cno > 0 && cno != null ){  // 2-1 : 카테고리별 조회
            productEntityList = productEntityRepository.findByCategoryEntityCno( cno );
        }else{   // 2-2 : 전체 조회
            productEntityList = productEntityRepository.findAll();
        }
        // 3. 조회한 결과 entity 를 dto 로 변환
        return productEntityList.stream()
                .map( ProductDto :: toDto )
                .collect( Collectors.toList() );
    }

    // 3. 제품 개별조회 : 설계 : ?pno=1

}














