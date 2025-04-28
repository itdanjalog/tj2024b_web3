package web.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.multipart.MultipartFile;
import web.model.dto.CategoryDto;
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

//    // 2. (카테고리별) 제품 전체조회 : 설계 : (카테고리조회)?cno=3  , (전체조회)?cno
//    public List<ProductDto> allProducts( Long cno ){
//        // 1. 조회된 결과를 저장하는 리스트 변수
//        List<ProductEntity> productEntityList;
//        // 2. cno 에 따라 카테고리별 조회 vs 전체조회
//        if( cno != null && cno > 0 ){  // 2-1 : 카테고리별 조회
//            productEntityList = productEntityRepository.findByCategoryEntityCno( cno );
//        }else{   // 2-2 : 전체 조회
//            productEntityList = productEntityRepository.findAll();
//        }
//        // 3. 조회한 결과 entity 를 dto 로 변환
//        return productEntityList.stream()
//                .map( ProductDto :: toDto )
//                .collect( Collectors.toList() );
//    }

    // 3. 제품 개별조회 : 설계 : ?pno=1
    public ProductDto viewProduct( long pno ){
        // 1. pno 에 해당하는 엔티티 조회
        Optional< ProductEntity > productEntityOptional =
            productEntityRepository.findById( pno );
        // 2. 조회 결과 없으면 null
        if( productEntityOptional.isEmpty() ) return null;
        // 3. 조회 결과 있으면 엔티티 꺼내기  .get()
        ProductEntity productEntity =
                productEntityOptional.get();
        // 4. 조회수 증가 , 기존 조회수 호출해서 + 1 결과를 저장
        productEntity.setPview( productEntity.getPview() +1 );
        // 5. 조회된 엔티티를 DTO 로 변환
        return ProductDto.toDto( productEntity );
    }

    // 4. 제품 개별삭제 , +이미지 삭제
    public boolean deleteProduct( long pno , int loginMno ){
        // 1. pno 에 해당 하는 엔티티 찾기
        Optional<ProductEntity> productEntityOptional =
            productEntityRepository.findById( pno );
        // 2. 없으면 false
        if( productEntityOptional.isEmpty() ) return false;
        // 3. 요청한 사람이 등록한 제품인지 확인
        ProductEntity productEntity = productEntityOptional.get();
        if( productEntity.getMemberEntity().getMno() != loginMno ){
            // 만약에 제품 등록한 회원의 번호 와 현재 로그인된 회원번호가 일치하지 않으면 false
            return false;
        }
        // 4. 서버에 저장된 (업로드) 이미지들 삭제
        List<ImgEntity> imgEntityList = productEntity.getImgEntityList();
        for( ImgEntity imgEntity : imgEntityList ){
            boolean result = fileUtil.fileDelete( imgEntity.getIname() ); // web2 작성한 파일삭제 메소드 참고
            if( result == false ){
                throw new RuntimeException("파일삭제 실패"); // 트랜잭션 롤백.
            }
        }
        // 5. 이미지 모두 삭제 했으면 제품 DB 삭제 , ?? 이미지 db 는 삭제 코드는 별도로 없다.
        // cascade = CascadeType.ALL 관계로 제품이 삭제되면 (FK)이미지 레코드로 같이 삭제한다.
        productEntityRepository.deleteById( pno );
        return true;
    }

    // 5. 제품 수정 ( + 이미지 추가 )
    public boolean updateProduct( ProductDto productDto , int loginMno ){
        // 1. 기존의 제품 정보(엔티티) 가져오기 // 없으면 취소 // Optional 클래스는 null 제어 메소드 제공
        Optional< ProductEntity > productEntityOptional = productEntityRepository.findById( productDto.getPno() );
        if( productEntityOptional.isEmpty() ) return false; // 조회 엔티티가 없으면
        ProductEntity productEntity = productEntityOptional.get();
        // 2. 현재 토큰(로그인) 사람의 등록한 제품인지 인가확인   // 아니면 취소
        if( productEntity.getMemberEntity().getMno() != loginMno ) return false;
        // 3. 현재 수정할 카테고리 엔티티 가져오기   // 없으면 취소
        Optional<CategoryEntity> categoryEntityOptional = categoryEntityRepository.findById( productDto.getCno() );
        if( categoryEntityOptional.isEmpty() ) return false;
        CategoryEntity categoryEntity = categoryEntityOptional.get();
        // 4. 제품 정보를 수정한다.  // 오류 발생시 롤백한다.    // - 조회한 기존의 제품 정보(엔티티) 에서 set 이용한 수정
        productEntity.setPname( productDto.getPname() );
        productEntity.setPcontent( productDto.getPcontent() );
        productEntity.setPprice( productDto.getPprice() );
        productEntity.setCategoryEntity( categoryEntity ); // 찾은 엔티티
        // 5. 새로운 이미지가 있으면 fileUtil 에서 업로드함수 이용하여 업로드한다.  // 오류 발생시 롤백한다.
        List< MultipartFile > newFile = productDto.getFiles();
        if( newFile != null && !newFile.isEmpty() ){ // 새로운 이미지가 존재하면
            for( MultipartFile file : newFile ){
                String saveFileName = fileUtil.fileUpload( file );
                if( saveFileName == null ) throw new RuntimeException("파일 업로드 오류발생"); // throw new 예외클래스명(); // 강제 예외 발생했다.
                ImgEntity imgEntity = ImgEntity.builder() // 이미지 엔티티 생성 , , 자바객체 <--영속X--> DB테이블레코드
                        .iname( saveFileName)
                        .productEntity( productEntity )
                        .build();
                imgEntityRepository.save( imgEntity ); // 이미지 엔티티 저장(영속) , 자바객체 <--영속O--> DB테이블레코드
            }
        }
        return true; // 6. 끝
    } // class end

    // 6. 이미지 개별 삭제
    public boolean deleteImage( long ino , int loginMno ){
        // 1. 이미지 엔티티 조회
        Optional<ImgEntity> optionalImgEntity  = imgEntityRepository.findById( ino );
        if( optionalImgEntity.isEmpty() ) return false;
        ImgEntity imgEntity = optionalImgEntity.get();
        // 2. 인가 확인 , 이미지 등록한 회원 == 제품을 등록한 회원
        if( imgEntity.getProductEntity().getMemberEntity().getMno() != loginMno ) return false;
        // 3. 물리적인 로컬 삭제.
        String deleteFileName = imgEntity.getIname();
        boolean result = fileUtil.fileDelete( deleteFileName );
        if( result == false  ) throw new RuntimeException("파일 삭제 실패 ");
        // 4. 엔티티 삭제 
        imgEntityRepository.deleteById( ino );
        return true; // 5. 끝
    }


    // 7. 카테고리 조회
    public List<CategoryDto> allCategory(){
        // 1. 모든 카테고리 조회
        List<CategoryEntity> categoryEntityList = categoryEntityRepository.findAll();
        // 2. List<Entity> --> List<Dto> 변환
        List<CategoryDto> categoryDtoList = categoryEntityList.stream()
                .map( CategoryDto::toDto )
                .collect( Collectors.toList() );
        // 3. 끝
        return categoryDtoList;
    }

    // 2. 검색+페이징처리 , 위에서 작업한 2번 메소드 주석처리 후 진행
    // public List<ProductDto> allProducts( Long cno , int page , int size , String keyword ){
    public Page<ProductDto> allProducts( Long cno , int page , int size , String keyword ){
        // 1. 페이징처리 설정 ,  page-1 : 1페이지를 0으로 사용하므로 -1  , size : 페이지당자료개수 , pno 기준으로 내림차순
        Pageable pageable = PageRequest.of( page-1 , size , Sort.by(  Sort.Direction.DESC , "pno")  );
        // Pageable : 인터페이스 ,  import org.springframework.data.domain.Pageable;
        // PageRequest : 클래스(구현체)   // .of( 페이지번호[0부터] , 페이지별자료수 , 정렬 )
        // 2. 내가 만든 네이티브 쿼리로 엔티티 조회
            // 예시] 전체조회 productEntityRepository.findAll( pageable );
            // 예시] 카테고리별조회 : productEntityRepository.만든함수명( pageable );
        Page<ProductEntity> productEntities = productEntityRepository.findBySearch( cno , keyword , pageable );
        // 3. 반환타입
        // List<ProductDto> productDtoList = productEntities.stream().map( ProductDto::toDto ).collect( Collectors.toList() );
        Page<ProductDto> productDtoList = productEntities.map( ProductDto::toDto);
        return productDtoList; // 4. 끝
    }


}

































