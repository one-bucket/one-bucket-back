
-- Foreign key 무결성을 비활성화합니다.
SET REFERENTIAL_INTEGRITY FALSE;
-- 기존 데이터 삭제
TRUNCATE TABLE post;
TRUNCATE TABLE board;
TRUNCATE TABLE board_type;
TRUNCATE TABLE member;
TRUNCATE TABLE profile;
TRUNCATE TABLE university;
TRUNCATE TABLE pending_trade;
TRUNCATE TABLE likes_map;
TRUNCATE TABLE comment;
TRUNCATE TABLE post_images;
TRUNCATE TABLE member_roles;
TRUNCATE TABLE comment_replies;
TRUNCATE TABLE pending_trade_member;

-- 필요한 경우 추가적인 테이블도 삭제합니다.
-- TRUNCATE TABLE other_table;

-- Foreign key 무결성 복원
SET REFERENTIAL_INTEGRITY TRUE;

