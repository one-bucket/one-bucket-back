
-- default entity id of test Database start from 100
SET @id = 100;
SET @dateTime = '2020-01-01 22:22:22.22222';

-- board type 생성
INSERT INTO board_type (id, description, name)
VALUES (100, 'description', 'board_type');

-- board 생성
INSERT INTO board (id, description, name, board_type_id, university_id)
VALUES (100, 'description', 'board', @id, @id);

-- post 생성
INSERT INTO post (is_modified, author_id, board_id, created_date, id, modified_date, text, title)
VALUES(0, @id, @id, @dateTime, @id, @dateTime, 'text of post', 'title of post');

-- comment - parent 생성
INSERT INTO comment (is_modified, author_id, created_date, id, modified_date, parent_comment_id, post_id, text)
VALUES (0, @id,  @dateTime, @id, @dateTime, null, @id, 'text of comment');

-- comment - reply 생성
INSERT INTO comment (is_modified, author_id, created_date, id, modified_date, parent_comment_id, post_id, text)
VALUES (0, @id, @dateTime, @id + 1, @dateTime, @id, @id, 'text of reply');

INSERT INTO comment_replies (comment_id, replies_id)
VALUES (@id, @id + 1);