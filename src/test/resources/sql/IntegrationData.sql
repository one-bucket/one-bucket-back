-- University 테이블 데이터 삽입
INSERT INTO university (id, address, email, name) VALUES
(1, 'university1', 'address1', '1@email.com'),
(2, 'university2', 'address2', '2@email.com'),
(3, 'university3', 'address3', '3@email.com'),
(4, 'university4', 'address4', '4@email.com'),
(5, 'university5', 'address5', '5@email.com');

INSERT INTO member (id, university_id, nickname, password, username, is_account_non_expired, is_account_non_locked, is_credential_non_expired, is_enable) VALUES
-- 대학 1의 회원
(1, 1, 'nickname1', 'password1', 'username1', true, true, true, true),
(2, 1, 'nickname2', 'password2', 'username2', true, true, true, true),
(3, 1, 'nickname3', 'password3', 'username3', true, true, true, true),
(4, 1, 'nickname4', 'password4', 'username4', true, true, true, true),
(5, 1, 'nickname5', 'password5', 'username5', true, true, true, true),

-- 대학 2의 회원
(6, 2, 'nickname6', 'password6', 'username6', true, true, true, true),
(7, 2, 'nickname7', 'password7', 'username7', true, true, true, true),
(8, 2, 'nickname8', 'password8', 'username8', true, true, true, true),
(9, 2, 'nickname9', 'password9', 'username9', true, true, true, true),
(10, 2, 'nickname10', 'password10', 'username10', true, true, true, true),

-- 대학 3의 회원
(11, 3, 'nickname11', 'password11', 'username11', true, true, true, true),
(12, 3, 'nickname12', 'password12', 'username12', true, true, true, true),
(13, 3, 'nickname13', 'password13', 'username13', true, true, true, true),
(14, 3, 'nickname14', 'password14', 'username14', true, true, true, true),
(15, 3, 'nickname15', 'password15', 'username15', true, true, true, true),

-- 대학 4의 회원
(16, 4, 'nickname16', 'password16', 'username16', true, true, true, true),
(17, 4, 'nickname17', 'password17', 'username17', true, true, true, true),
(18, 4, 'nickname18', 'password18', 'username18', true, true, true, true),
(19, 4, 'nickname19', 'password19', 'username19', true, true, true, true),
(20, 4, 'nickname20', 'password20', 'username20', true, true, true, true),

-- 대학 5의 회원
(21, 5, 'nickname21', 'password21', 'username21', true, true, true, true),
(22, 5, 'nickname22', 'password22', 'username22', true, true, true, true),
(23, 5, 'nickname23', 'password23', 'username23', true, true, true, true),
(24, 5, 'nickname24', 'password24', 'username24', true, true, true, true),
(25, 5, 'nickname25', 'password25', 'username25', true, true, true, true);

INSERT INTO board_type (id, name, description) VALUES
(1, 'type1', 'description1'),
(2, 'type2', 'description2'),
(3, 'type3', 'description3');

INSERT INTO board (id, university_id, board_type_id, name, description) VALUES
(1, 1, 1, 'board1', 'description1'),
(2, 1, 2, 'board2', 'description2'),
(3, 1, 3, 'board3', 'description3'),

(4, 2, 1, 'board4', 'description4'),
(5, 2, 2, 'board5', 'description5'),
(6, 2, 3, 'board6', 'description6'),

(7, 3, 1, 'board7', 'description7'),
(8, 3, 2, 'board8', 'description8'),
(9, 3, 3, 'board9', 'description9'),

(10, 4, 1, 'board10', 'description10'),
(11, 4, 2, 'board11', 'description11'),
(12, 4, 3, 'board12', 'description12'),

(13, 5, 1, 'board13', 'description13'),
(14, 5, 2, 'board14', 'description14'),
(15, 5, 3, 'board15', 'description15');
