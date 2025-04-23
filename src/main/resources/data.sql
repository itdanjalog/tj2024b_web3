-- data.sql 샘플 데이터 (비밀번호 BCrypt 적용)

-- Member 테이블 샘플 데이터 삽입 (mpwd는 BCrypt로 인코딩된 값 사용)
-- !!! 아래 해시값은 예시입니다. 실제 생성된 해시값으로 교체하세요. !!!
INSERT INTO member (memail, mpwd, mname, create_at, update_at) VALUES ('user1@example.com', '$2a$10$EXAMPLEHASHFOR1234.ABCDEFGHIJKLMNOPQRSTUVWXYZabc', '김회원', NOW(), NOW()); -- '1234'의 BCrypt 해시 (예시)
INSERT INTO member (memail, mpwd, mname, create_at, update_at) VALUES ('user2@example.com', '$2a$10$EXAMPLEHASHFOR5678.ABCDEFGHIJKLMNOPQRSTUVWXYZdef', '이사용', NOW(), NOW()); -- '5678'의 BCrypt 해시 (예시)
INSERT INTO member (memail, mpwd, mname, create_at, update_at) VALUES ('admin@example.com', '$2a$10$EXAMPLEHASHFORADMIN.ABCDEFGHIJKLMNOPQRSTUVWXYZghi', '박관리', NOW(), NOW()); -- 'admin'의 BCrypt 해시 (예시)

-- Category 테이블 샘플 데이터 삽입 (이전과 동일)
INSERT INTO category (cname, create_at, update_at) VALUES ('전자제품', NOW(), NOW());
INSERT INTO category (cname, create_at, update_at) VALUES ('도서', NOW(), NOW());
INSERT INTO category (cname, create_at, update_at) VALUES ('의류', NOW(), NOW());

-- Product 테이블 샘플 데이터 삽입 (이전과 동일)
INSERT INTO product (pname, pcontent, pprice, pview, mno, cno, create_at, update_at) VALUES ('샘플 노트북', '고성능 노트북입니다.', 1500000, 10, 1, 1, NOW(), NOW());
INSERT INTO product (pname, pcontent, pprice, pview, mno, cno, create_at, update_at) VALUES ('JPA 프로그래밍', '자바 ORM 표준 JPA 프로그래밍 책', 35000, 5, 1, 2, NOW(), NOW());
INSERT INTO product (pname, pcontent, pprice, pview, mno, cno, create_at, update_at) VALUES ('스프링 부트 3', '최신 스프링 부트 가이드', 40000, 2, 2, 2, NOW(), NOW());
INSERT INTO product (pname, pcontent, pprice, pview, mno, cno, create_at, update_at) VALUES ('반팔 티셔츠', '편안한 면 티셔츠', 25000, 0, 2, 3, NOW(), NOW());