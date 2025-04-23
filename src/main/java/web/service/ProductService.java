package web.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    // 2. 전체 또는 카테고리별 + 검색어별 제품 조회 (네이티브 쿼리, 페이징 없음)
    // 반환 타입을 List<ProductDto>로, 파라미터에서 Pageable 제거
    public List<ProductDto> allProducts(Long cno, String keyword) {
        // 1. 검색 키워드 유효성 검사 (if문 사용)
        String searchKeyword = null;
        if (keyword != null && !keyword.trim().isEmpty()) {
            searchKeyword = keyword;
        }

        Pageable pageable = PageRequest.of( 0 , 3 );

        // 2. Repository의 네이티브 쿼리 메소드 호출 (Pageable 없이)
        Page<ProductEntity> productEntityList = productEntityRepository.findBySearchFilters(cno, searchKeyword , pageable );

        // 3. Entity 리스트를 Dto 리스트로 변환하여 반환
        return productEntityList.stream()
                .map(ProductDto::toDto)
                .collect(Collectors.toList());
    }

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

    // --- 5. 개별 제품 수정 (pno 기준: 이름, 내용, 가격 수정 및 이미지 추가) ---
    public boolean updateProduct(ProductDto productDto ,  int loggedInMemberMno) {

        long pno = productDto.getPno();
        // 1. 기존 제품 정보 조회
        Optional<ProductEntity> optionalProductEntity = productEntityRepository.findById(pno);
        if (optionalProductEntity.isEmpty()) {
            System.out.println("Error: Product not found for update with pno: " + pno);
            return false; // 수정할 제품 없음
        }
        ProductEntity productEntity = optionalProductEntity.get();

        // 2. 인가 확인: 현재 로그인된 사용자가 제품 작성자인지 확인
        if (productEntity.getMemberEntity().getMno() != loggedInMemberMno) {
            System.out.println("Error: Unauthorized product update attempt for pno: " + pno);
            return false; // 권한 없음
        }

        // 3. 선택된 카테고리 정보 조회 및 확인
        Optional<CategoryEntity> optionalCategoryEntity = categoryEntityRepository.findById(productDto.getCno());
        if (optionalCategoryEntity.isEmpty()) {
            System.out.println("Error: Category not found.");
            return false; // 카테고리 정보 없음
        }
        CategoryEntity categoryEntity = optionalCategoryEntity.get();

        // 3. 제품 정보 업데이트 (이름, 내용, 가격)
        // DTO에서 받은 값이 null이 아니거나 비어있지 않을 때만 업데이트 (선택적)
        // 여기서는 DTO에 값이 있으면 무조건 업데이트한다고 가정
        productEntity.setPname(productDto.getPname());
        productEntity.setPcontent(productDto.getPcontent());
        productEntity.setPprice(productDto.getPprice());
        // 참고: category(cno)는 이번 요청에서 수정하지 않음
        productEntity.setCategoryEntity( categoryEntity );

        List<MultipartFile> newFiles = productDto.getFiles();
        // 4. 새로운 이미지 추가 처리
        if (newFiles != null && !newFiles.isEmpty()) {
            for (MultipartFile file : newFiles) {
                if (file.isEmpty()) continue; // 비어있는 파일은 건너뛰기

                // 파일 업로드 및 저장
                String savedFileName = fileUtil.fileUpload(file);
                if (savedFileName == null) {
                    System.err.println("File upload failed during update for: " + file.getOriginalFilename());
                    // 업로드 실패 시 롤백을 위해 예외 발생
                    throw new RuntimeException("File upload failed during update, rolling back transaction.");
                }

                // 새 ImgEntity 생성 및 저장
                ImgEntity newImgEntity = ImgEntity.builder()
                        .iname(savedFileName)
                        .productEntity(productEntity) // 현재 수정 중인 제품 엔티티와 연결
                        .build();
                imgEntityRepository.save(newImgEntity); // 새 이미지 정보 저장
            }
        }

        // 5. 변경된 ProductEntity 저장 (JPA 변경 감지로 인해 명시적 save는 필수는 아님)
        // productRepository.save(productEntity); // 명시적으로 호출해도 무방

        return true; // 수정 성공
    }

    // --- 4. 개별 이미지 삭제 (ino 기준) ---
    public boolean deleteProductImage(long ino, int loginMno) {
        // 1. 이미지 정보 조회
        Optional<ImgEntity> optionalImgEntity = imgEntityRepository.findById(ino);
        if (optionalImgEntity.isEmpty()) {
            System.out.println("Error: Image not found for deletion with ino: " + ino);
            return false; // 삭제할 이미지 없음
        }
        ImgEntity imgEntity = optionalImgEntity.get();

        // 2. 이미지에 연결된 제품 정보 확인 (권한 체크용)
        ProductEntity productEntity = imgEntity.getProductEntity();
        if (productEntity == null || productEntity.getMemberEntity() == null) {
            System.err.println("Error: Image or associated product/member data is inconsistent for ino: " + ino);
            // 데이터 무결성 문제 발생 가능성, 로깅 및 조사 필요
            return false; // 비정상 상태
        }

        // 3. 인가 확인: 현재 로그인된 사용자가 제품 작성자인지 확인
        if (productEntity.getMemberEntity().getMno() != loginMno) {
            System.out.println("Error: Unauthorized image deletion attempt for ino: " + ino);
            return false; // 권한 없음
        }

        // 4. 물리적 파일 삭제
        String filename = imgEntity.getIname();
        boolean fileDeleted = fileUtil.fileDelete(filename);
        if (!fileDeleted) {
            // 파일 삭제 실패 시 처리 (로그 남기기 등)
            System.err.println("Failed to delete image file: " + filename + " for ino: " + ino);
            // 중요도에 따라 여기서 false를 반환하거나, DB 삭제는 진행할지 결정
            // 여기서는 파일 삭제 실패 시에도 DB 삭제는 시도하지 않도록 false 반환 (혹은 예외 발생)
            // return false; // 파일 삭제 실패 시 작업 중단 원할 경우
            throw new RuntimeException("Failed to delete image file: " + filename); // 또는 예외 발생시켜 롤백
        }

        // 5. DB에서 이미지 정보 삭제
        imgEntityRepository.delete(imgEntity);

        return true; // 이미지 삭제 성공
    }

    public Map<Long, String> getAllCategory() {
        // 1. 모든 카테고리 엔티티를 조회합니다.
        List<CategoryEntity> categoryEntityList = categoryEntityRepository.findAll();

        // 2. 조회된 리스트를 Stream API를 사용하여 Map으로 변환합니다.
        //    - CategoryEntity::getCno : 카테고리 번호를 Map의 Key로 사용합니다.
        //    - CategoryEntity::getCname : 카테고리 이름을 Map의 Value로 사용합니다.
        Map<Long , String > categoryMap = new HashMap<>();
        for(CategoryEntity categoryEntity : categoryEntityList ){
            categoryMap.put( categoryEntity.getCno() , categoryEntity.getCname() );
        }
        // 3. 생성된 Map을 반환합니다.
        return categoryMap;
    }



}








