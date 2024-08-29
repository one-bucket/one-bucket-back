-- default entity id of test Database start from 100
SET @id = 100;


-- member 추가
INSERT INTO member (id, nickname, password, username,
                    is_account_non_expired, is_account_non_locked, is_credential_non_expired, is_enable)
VALUES (@id, 'nickname', '!1Password1!', 'username', 1, 1, 1, 1);

-- member role 추가
INSERT INTO member_roles (member_id, roles)
VALUES (@id, 'USER');

-- 대학 추가
INSERT INTO university (id, address, email, name)
VALUES (@id, `address`, 'email@com', 'university');

-- 사용자의 대학 업데이트
UPDATE member SET university_id = @id WHERE id = @id;

-- 프로필 생성
INSERT INTO profile (id, age, birth, create_at, description, gender, is_basic_image, name, update_at)
VALUES (@id, 20, '1999-01-01', '2020-01-01 22:22:22.222222', 'description', 'man', 1, 'name', '2020-01-01 22:22:22.222222');

-- board type 생성
INSERT INTO board_type (id, description, name)
VALUES (100, 'description', 'board_type');

-- board 생성
INSERT INTO board (id, description, name, board_type_id, university_id)
VALUES (100, 'description', 'board', @id, @id);

INSERT INTO post (dtype, id, create_date, modified_date, text, title, is_fin, item, joins, location, wanted, author_id, board_id)
VALUES()









