
-- Foreign key 무결성을 비활성화합니다.
SET REFERENTIAL_INTEGRITY FALSE;

-- 기존 데이터 삭제
TRUNCATE TABLE post;
TRUNCATE TABLE board;
TRUNCATE TABLE board_type;
TRUNCATE TABLE member;
TRUNCATE TABLE university;

-- 필요한 경우 추가적인 테이블도 삭제합니다.
-- TRUNCATE TABLE other_table;

-- Foreign key 무결성 복원
SET REFERENTIAL_INTEGRITY TRUE;
