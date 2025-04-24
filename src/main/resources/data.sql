-- Member 테이블 (10명) - 비밀번호는 해시, 주석에는 원문 표시
-- 모든 회원의 원본 비밀번호는 '1234' (BCrypt 해시 포함)
INSERT INTO member (memail, mpwd, mname, create_at, update_at) VALUES
('user1@example.com',  '$2a$10$ces9K/Dj7MUvZT4z5Z53yent9GqwfzsEhGuPNA9Ct6NzXq6ZHEXw.', '김하나', NOW(), NOW()), -- 비밀번호: 1234
('user2@example.com',  '$2a$10$ces9K/Dj7MUvZT4z5Z53yent9GqwfzsEhGuPNA9Ct6NzXq6ZHEXw.', '이둘',   NOW(), NOW()), -- 비밀번호: 1234
('user3@example.com',  '$2a$10$ces9K/Dj7MUvZT4z5Z53yent9GqwfzsEhGuPNA9Ct6NzXq6ZHEXw.', '박셋',   NOW(), NOW()), -- 비밀번호: 1234
('user4@example.com',  '$2a$10$ces9K/Dj7MUvZT4z5Z53yent9GqwfzsEhGuPNA9Ct6NzXq6ZHEXw.', '최넷',   NOW(), NOW()), -- 비밀번호: 1234
('user5@example.com',  '$2a$10$ces9K/Dj7MUvZT4z5Z53yent9GqwfzsEhGuPNA9Ct6NzXq6ZHEXw.', '정다섯', NOW(), NOW()), -- 비밀번호: 1234
('user6@example.com',  '$2a$10$ces9K/Dj7MUvZT4z5Z53yent9GqwfzsEhGuPNA9Ct6NzXq6ZHEXw.', '한여섯', NOW(), NOW()), -- 비밀번호: 1234
('user7@example.com',  '$2a$10$ces9K/Dj7MUvZT4z5Z53yent9GqwfzsEhGuPNA9Ct6NzXq6ZHEXw.', '노일곱', NOW(), NOW()), -- 비밀번호: 1234
('user8@example.com',  '$2a$10$ces9K/Dj7MUvZT4z5Z53yent9GqwfzsEhGuPNA9Ct6NzXq6ZHEXw.', '오여덟', NOW(), NOW()), -- 비밀번호: 1234
('user9@example.com',  '$2a$10$ces9K/Dj7MUvZT4z5Z53yent9GqwfzsEhGuPNA9Ct6NzXq6ZHEXw.', '장아홉', NOW(), NOW()), -- 비밀번호: 1234
('user10@example.com', '$2a$10$ces9K/Dj7MUvZT4z5Z53yent9GqwfzsEhGuPNA9Ct6NzXq6ZHEXw.', '윤열',   NOW(), NOW()); -- 비밀번호: 1234

-- Category 테이블 (3개)
INSERT INTO category (cname, create_at, update_at) VALUES
('전자제품', NOW(), NOW()),
('도서', NOW(), NOW()),
('의류', NOW(), NOW());

-- Product 테이블 (30개)
INSERT INTO product (pname, pcontent, pprice, pview, mno, cno, create_at, update_at) VALUES
('노트북 A', '성능 좋은 노트북 A', 1300000, 5, 1, 1, NOW(), NOW()),
('노트북 B', '성능 좋은 노트북 B', 1450000, 3, 2, 1, NOW(), NOW()),
('무선 이어폰', '편리한 무선 이어폰', 120000, 10, 3, 1, NOW(), NOW()),
('스마트폰', '최신형 스마트폰', 980000, 20, 4, 1, NOW(), NOW()),
('태블릿', '휴대용 태블릿', 600000, 6, 5, 1, NOW(), NOW()),
('JPA 책', '자바 ORM 책', 35000, 4, 1, 2, NOW(), NOW()),
('Spring Boot 책', '스프링 부트 완벽 가이드', 40000, 3, 2, 2, NOW(), NOW()),
('Flutter 책', '플러터 개발 실전', 38000, 2, 3, 2, NOW(), NOW()),
('Clean Code', '클린 코드 원서', 50000, 8, 4, 2, NOW(), NOW()),
('Effective Java', '자바 베스트 셀러', 45000, 7, 5, 2, NOW(), NOW()),
('티셔츠', '면 티셔츠', 25000, 0, 6, 3, NOW(), NOW()),
('청바지', '캐주얼 청바지', 55000, 1, 7, 3, NOW(), NOW()),
('자켓', '봄 가을 자켓', 80000, 2, 8, 3, NOW(), NOW()),
('운동화', '편한 운동화', 70000, 5, 9, 3, NOW(), NOW()),
('모자', '야구 모자', 18000, 3, 10, 3, NOW(), NOW()),
('블루투스 스피커', '작고 강력한 사운드', 89000, 4, 6, 1, NOW(), NOW()),
('전동 면도기', '깔끔한 전동 면도기', 54000, 2, 7, 1, NOW(), NOW()),
('키보드', '기계식 키보드', 75000, 9, 8, 1, NOW(), NOW()),
('마우스', '무선 마우스', 30000, 6, 9, 1, NOW(), NOW()),
('USB 메모리', '32GB USB 3.0', 12000, 1, 10, 1, NOW(), NOW()),
('도커 책', '도커 실전 입문', 43000, 2, 1, 2, NOW(), NOW()),
('리액트 책', '리액트 프로그래밍', 37000, 5, 2, 2, NOW(), NOW()),
('알고리즘 책', '자료구조와 알고리즘', 47000, 4, 3, 2, NOW(), NOW()),
('운영체제 책', 'OS 기본 이론', 52000, 3, 4, 2, NOW(), NOW()),
('네트워크 책', '컴퓨터 네트워크 기초', 41000, 6, 5, 2, NOW(), NOW()),
('셔츠', '남성용 셔츠', 43000, 2, 6, 3, NOW(), NOW()),
('반바지', '여름용 반바지', 30000, 1, 7, 3, NOW(), NOW()),
('점퍼', '겨울용 점퍼', 99000, 4, 8, 3, NOW(), NOW()),
('레깅스', '여성용 레깅스', 27000, 3, 9, 3, NOW(), NOW()),
('후드티', '기모 후드티', 59000, 5, 10, 3, NOW(), NOW());