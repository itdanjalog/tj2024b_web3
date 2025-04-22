package web.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
// import org.springframework.beans.factory.annotation.Value; // FileUtil 사용으로 불필요
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import web.model.dto.ProductDto;
import web.model.entity.*;
import web.model.repository.*; // 모든 Repository import 가정
import web.util.FileUtil; // 제공된 FileUtil import

// import java.io.File; // FileUtil 사용으로 불필요
// import java.io.IOException; // FileUtil에서 처리
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
// import java.util.UUID; // FileUtil에서 처리

@Service
@RequiredArgsConstructor // final 필드에 대한 생성자 자동 주입 (Lombok)
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final MemberEntityRepository memberRepository; // 작성자 정보 조회
    private final CategoryRepository categoryRepository; // 카테고리 정보 조회
    private final ImgRepository imgRepository; // 이미지 정보 저장
    private final FileUtil fileUtil; // 파일 처리를 위한 FileUtil 주입

    // 파일 저장 경로는 FileUtil 내부에 정의되어 있으므로 @Value 제거
    // @Value("${file.upload.path}")
    // private String uploadPath;

    public boolean registerProduct(ProductDto productDto, int loginMno) {

        // 1. 현재 로그인된 회원 정보 조회 및 확인
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findById(loginMno);
        if (optionalMemberEntity.isEmpty()) {
            System.out.println("Error: Member not found.");
            return false; // 회원 정보 없음
        }
        MemberEntity memberEntity = optionalMemberEntity.get();

        // 2. 선택된 카테고리 정보 조회 및 확인
        Optional<CategoryEntity> optionalCategoryEntity = categoryRepository.findById(productDto.getCno());
        if (optionalCategoryEntity.isEmpty()) {
            System.out.println("Error: Category not found.");
            return false; // 카테고리 정보 없음
        }
        CategoryEntity categoryEntity = optionalCategoryEntity.get();

        // 3. ProductDto를 ProductEntity로 변환 (작성자, 카테고리 정보 포함)
        ProductEntity productEntity = productDto.toEntity();
        productEntity.setMemberEntity( memberEntity );
        productEntity.setCategoryEntity( categoryEntity );

        // 4. ProductEntity 저장 (DB에 저장 후 pno 자동 생성)
        ProductEntity savedProduct = productRepository.save(productEntity);
        if (savedProduct.getPno() <= 0) {
            System.out.println("Error: Failed to save product.");
            return false; // 제품 저장 실패
        }

        // 5. 이미지 파일 처리 및 ImgEntity 저장 (FileUtil 사용)
        if (productDto.getFiles() != null && !productDto.getFiles().isEmpty()) {
            for (MultipartFile file : productDto.getFiles()) {
                if (file.isEmpty()) continue; // 파일이 비어있으면 건너뛰기

                // FileUtil을 사용하여 파일 업로드 및 저장된 파일명 받기
                String savedFileName = fileUtil.fileUpload(file);

                if (savedFileName == null) {
                    // 파일 업로드 실패 시 처리
                    System.err.println("File upload failed for: " + file.getOriginalFilename());
                    // 실패 시 트랜잭션 롤백을 위해 예외 발생 또는 명시적 롤백 처리 필요
                    // 여기서는 RuntimeException을 발생시켜 @Transactional이 롤백하도록 함
                    throw new RuntimeException("File upload failed, rolling back transaction.");
                    // 또는 return false; 를 사용하고 싶다면 @Transactional의 rollbackFor 설정을 확인해야 할 수 있음
                    // return false;
                }

                // ImgEntity 생성 및 저장
                ImgEntity imgEntity = ImgEntity.builder()
                        .iname(savedFileName) // FileUtil이 반환한 저장된 파일명 사용
                        .productEntity(savedProduct) // 방금 저장된 ProductEntity 참조
                        .build();
                imgRepository.save(imgEntity);
            }
        }

        return true; // 모든 과정 성공
    }

    // --- 1. 전체 또는 카테고리별 제품 조회 ---
    public List<ProductDto> getAllProducts(Long cno) { // Long 타입으로 받아 null 체크
        List<ProductEntity> productEntities;

        if (cno != null && cno > 0) { // cno가 제공되었고 유효한 값(0 초과)이라면
            // CategoryRepository를 통해 CategoryEntity 존재 여부 확인 후 조회할 수도 있음 (선택적)
            // 예: if (!categoryRepository.existsById(cno)) return Collections.emptyList();

            // 특정 카테고리 제품 조회
            productEntities = productRepository.findByCategoryEntityCno(cno);
        } else {
            // cno가 없거나 유효하지 않으면 전체 제품 조회
            productEntities = productRepository.findAll();
        }

        // 조회된 Entity 리스트를 Dto 리스트로 변환하여 반환
        return productEntities.stream()
                .map(ProductDto::toDto) // 정적 메소드 참조
                .collect(Collectors.toList());
    }

    // --- 2. 개별 제품 조회 (pno 기준) ---
    public ProductDto getProductByPno(long pno) {
        Optional<ProductEntity> optionalProductEntity = productRepository.findById(pno);

        if (optionalProductEntity.isPresent()) {
            ProductEntity productEntity = optionalProductEntity.get();

            // 조회수 증가 로직
            productEntity.setPview(productEntity.getPview() + 1);
            // productRepository.save(productEntity); // 변경 감지(Dirty Checking)에 의해 @Transactional 내에서는 보통 자동 저장됨

            // Entity를 Dto로 변환하여 반환
            return ProductDto.toDto(productEntity);
        } else {
            return null; // 제품이 없을 경우 null 반환
        }
    }

    // --- 3. 개별 제품 삭제 (pno 기준) ---
    public boolean deleteProduct(long pno, int loginMno) {
        Optional<ProductEntity> optionalProductEntity = productRepository.findById(pno);

        if (optionalProductEntity.isEmpty()) {
            System.out.println("Error: Product not found for deletion.");
            return false; // 삭제할 제품 없음
        }

        ProductEntity productEntity = optionalProductEntity.get();

        // 인가 확인: 현재 로그인된 사용자가 제품 작성자인지 확인
        if (productEntity.getMemberEntity().getMno() != loginMno) {
            System.out.println("Error: Unauthorized deletion attempt.");
            // 관리자 권한 체크 로직을 추가할 수도 있음
            return false; // 권한 없음
        }

        // 제품 삭제 전 연결된 물리적 이미지 파일 삭제
        List<ImgEntity> imgEntities = imgRepository.findByProductEntity(productEntity);
        for (ImgEntity imgEntity : imgEntities) {
            boolean deleted = fileUtil.fileDelete(imgEntity.getIname());
            if (!deleted) {
                // 파일 삭제 실패 시 로그 남기기 (트랜잭션은 계속 진행될 수 있음, 또는 예외 발생시켜 롤백)
                System.err.println("Failed to delete image file: " + imgEntity.getIname());
                // 필요시 여기서 RuntimeException 발생시켜 전체 롤백 유도 가능
                throw new RuntimeException("Failed to delete associated image file: " + imgEntity.getIname());
            }
        }

        // 제품 엔티티 삭제
        // CascadeType.ALL 설정에 의해 연결된 ImgEntity, ReplyEntity도 DB에서 함께 삭제됨
        productRepository.delete(productEntity);

        return true; // 삭제 성공
    }

    // --- 4. 개별 이미지 삭제 (ino 기준) ---
    public boolean deleteProductImage(long ino, int loginMno) {
        // 1. 이미지 정보 조회
        Optional<ImgEntity> optionalImgEntity = imgRepository.findById(ino);
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
        imgRepository.delete(imgEntity);

        return true; // 이미지 삭제 성공
    }
    // --- 5. 개별 제품 수정 (pno 기준: 이름, 내용, 가격 수정 및 이미지 추가) ---
    public boolean updateProduct(ProductDto productDto ,  int loggedInMemberMno) {

        long pno = productDto.getPno();
        // 1. 기존 제품 정보 조회
        Optional<ProductEntity> optionalProductEntity = productRepository.findById(pno);
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
        Optional<CategoryEntity> optionalCategoryEntity = categoryRepository.findById(productDto.getCno());
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
                imgRepository.save(newImgEntity); // 새 이미지 정보 저장
            }
        }

        // 5. 변경된 ProductEntity 저장 (JPA 변경 감지로 인해 명시적 save는 필수는 아님)
        // productRepository.save(productEntity); // 명시적으로 호출해도 무방

        return true; // 수정 성공
    }



}