package com.onebucket.testComponent.testSupport;

import com.onebucket.global.exceptionManage.customException.boardManageException.UserBoardException;

import java.util.ArrayList;
import java.util.List;

/**
 * <br>package name   : com.onebucket.testComponent.testSupport
 * <br>file name      : BoardRestDocsSupport
 * <br>date           : 10/11/24
 * <pre>
 * <span style="color: white;">[description]</span>
 *
 * </pre>
 * <pre>
 * <span style="color: white;">usage:</span>
 * {@code
 *
 * } </pre>
 */
public class BoardRestDocsSupport extends UserRestDocsSupportTest {

    protected void createBoardType(Long id) {
        String description = "description" + id;
        String name = "type" + id;
        String query = """
                INSERT INTO board_type (id, description, name)
                VALUES (?, ?, ?)
                """;
        jdbcTemplate.update(query, id, description, name);
    }

    protected Long createUniversity() {

        String address = "address" + stackUnivId;
        String email = "email@email." + stackUnivId;
        String name = "univ" + stackUnivId;
        String query = """
                INSERT INTO university (id, address, email, name)
                VALUES (?, ?, ?, ?)
                """;
        jdbcTemplate.update(query, stackUnivId, address, email, name);
        return stackUnivId++;
    }

    protected void createBoard(Long count) {
        List<Long> univIds = new ArrayList<>();
        for(long i = 1; i <= count; i++) {
            createBoardType(i);
            univIds.add(createUniversity());
        }

        Long index = 1L;
        String query = """
                INSERT INTO board (id, description, name, board_type_id, university_id)
                VALUES (?, ?, ?, ?, ?)
                """;
        for(long i = 1; i <= count; i++) {
            for(long j = 1; j <= count; j++) {
                String name = "board" + i + j;
                String description = "description" + i + j;

                jdbcTemplate.update(query, index, description,name, j, univIds.get((int)i));
                index++;
            }
        }
    }

    protected void createPost(Long count, Long boardId) {

        String query = """
                INSERT INTO post (post_type, board_id, created_date, is_modified, likes, modified_date, text, title, views, author_id, pending_trade_id)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
                """;
        jdbcTemplate.update(query, 'post', );
    }
}
