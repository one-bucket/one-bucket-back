package com.onebucket.testComponent.testSupport;

import org.junit.jupiter.api.AfterEach;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.restdocs.payload.FieldDescriptor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

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

    protected Long stackBoardTypeId = 1L;
    protected Long stackBoardId = 1L;
    protected Long stackPostId = 1L;
    protected Long stackCommentId = 1L;
    protected Long stackTradeId = 1L;

    protected Long stackUnivId = 1L;


    @Override
    @AfterEach
    protected void after() {
        super.after();
        stackBoardTypeId = 1L;
        stackBoardId = 1L;
        stackPostId = 1L;
        stackCommentId = 1L;
        stackTradeId = 1L;
        stackUnivId = 1L;
    }

    protected Long createBoardType(int count) {
        Long startBoardTypeId = stackBoardTypeId;
        for(int i = 0; i < count; i++) {
            String description = "description" + stackBoardTypeId;
            String name = "type" + stackBoardTypeId;
            String query = """
                INSERT INTO board_type (id, description, name)
                VALUES (?, ?, ?)
                """;
            jdbcTemplate.update(query, stackBoardTypeId, description, name);
            stackBoardTypeId++;
        }

        return startBoardTypeId;
    }

    protected Long createUniversity(int count) {

        Long startUnivId = stackUnivId;
        for(int i = 0; i < count; i++) {
            String address = "address" + stackUnivId;
            String email = "email@email." + stackUnivId;
            String name = "univ" + stackUnivId;
            String query = """
                INSERT INTO university (id, address, email, name)
                VALUES (?, ?, ?, ?)
                """;
            jdbcTemplate.update(query, stackUnivId, address, email, name);
            stackUnivId++;
        }
        return startUnivId;
    }

    protected void setUniversityToUser(Long userId, Long univId) {
        String query = """
                UPDATE member
                set university_id = ?
                WHERE id = ?
                """;

        jdbcTemplate.update(query, userId, univId);
    }

    protected Long createBoard(Long univId, Long boardTypeId) {

        Long startBoardId = stackBoardId;
        String query = """
                INSERT INTO board (id, description, name, board_type_id, university_id)
                VALUES (?, ?, ?, ?, ?)
                """;
        String name = "board" + stackBoardId;
        String description = "description" + stackBoardId;
        jdbcTemplate.update(query, stackBoardId, description,name, boardTypeId, univId);

        stackBoardId++;
        return startBoardId;
    }

    protected Long createPost(int count, Long boardId, Long authorId) {

        Long startPostId = stackPostId;
        String query = """
                INSERT INTO post (id, post_type, board_id, created_date, is_modified, likes, modified_date, text, title, views, author_id, pending_trade_id)
                VALUES (?, 'Post', ?, '2024-01-01', false, 0, '2024-01-01', ?, ?, 0, ?, null);
                """;

        for(int i = 0; i < count; i++) {
            String text = "text" + stackPostId;
            String title = "title" + stackPostId;
            jdbcTemplate.update(query, stackPostId, boardId, text, title, authorId);
            stackPostId++;

        }
        return startPostId;
    }

    protected Long createComment(Long count, Long postId, Long authorId) {
        Long startCommentId = stackCommentId;

        String query = """
                INSERT INTO comment (id, created_date, is_modified, layer, modified_date, text, author_id, parent_comment_id, post_id)
                VALUES (?, '2024-01-01', false, 0, '2024-01-01', ?, ?, null, ?);
                """;
        for(int i = 0; i < count; i++) {
            String text = "text" + stackCommentId;

            jdbcTemplate.update(query, stackCommentId, text, authorId, postId);
            stackCommentId++;
        }
        return startCommentId;
    }

    protected Long createReply(Long count, Long postId, Long authorId, Long parentCommentId) {
        Long startCommentId = stackCommentId;
        String query = """
                INSERT INTO comment (id, created_date, is_modified, layer, modified_date, text, author_id, parent_comment_id, post_id)
                VALUES (?, '2024-01-01', false, 1, '2024-01-01', ?, ?, ?, ?);
                """;
        for(int i = 0; i < count; i++) {
            String text = "text" + stackCommentId;

            jdbcTemplate.update(query, stackCommentId, text, authorId, parentCommentId, postId);
        }
        return startCommentId;
    }

    protected void createTag() {
        String query = """
                INSERT INTO trade_tag (name)
                VALUES ('tag')
                """;

        jdbcTemplate.update(query);
    }

    protected Long createMarketPost(Long count, Long boardId, Long authorId) {

        Long startPostId = stackPostId;
        createTag();

        String createTradeQuery = """
                INSERT INTO project.pending_trade (id, count, due_date, finish_trade_at, is_fin, 
                                    item, joins, link_url, location, price, start_trade_at, 
                                    wanted, owner_id, tag_id) 
                VALUES (?, 5, ?, null, false, ?, 1, 'https://example.com', 'location', 1000, '2024-01-01', 5, ?, 'tag');
                """;

        String createMarketPostQuery = """
                INSERT INTO post (id, post_type, board_id, created_date, is_modified, likes, modified_date, text, title, views, author_id, pending_trade_id)
                VALUES (?, 'MarketPost', ?, '2024-01-01', false, 0, '2024-01-01', ?, ?, 0, ?, ?);
                """;
        for(int i = 0; i < count; i++) {
            String item = "item" + stackTradeId;
            String text = "text" + stackPostId;
            String title = "title" + stackPostId;
            jdbcTemplate.update(createTradeQuery, stackTradeId, LocalDate.now().plusDays(14), item, authorId);
            jdbcTemplate.update(createMarketPostQuery, stackPostId, boardId, text, title, authorId, stackTradeId);

            stackTradeId++;
            stackPostId++;
        }

        return startPostId;
    }

    // --------- Page / Pageable  관련 유틸리티 메서드
    protected <T> Page<T> toPage(List<T> list) {
        Pageable pageable = PageRequest.of(0, 20);
        return new PageImpl<>(list, pageable, list.size());
    }

    protected static List<FieldDescriptor> getPageCommonFields() {
        List<FieldDescriptor> fields = new ArrayList<>();

        fields.add(fieldWithPath("totalPages").description("Total number of pages"));
        fields.add(fieldWithPath("totalElements").description("Total number of elements"));
        fields.add(fieldWithPath("size").description("The size of the page"));
        fields.add(fieldWithPath("number").description("Current page number"));
        fields.add(fieldWithPath("numberOfElements").description("Number of elements in the current page"));
        fields.add(fieldWithPath("first").description("Whether this is the first page"));
        fields.add(fieldWithPath("last").description("Whether this is the last page"));
        fields.add(fieldWithPath("empty").description("Whether the content is empty"));

        // Sort 정보
        fields.add(fieldWithPath("sort.empty").description("Whether the sort is empty"));
        fields.add(fieldWithPath("sort.sorted").description("Whether the sort is sorted"));
        fields.add(fieldWithPath("sort.unsorted").description("Whether the sort is unsorted"));

        // Pageable 정보
        fields.add(fieldWithPath("pageable.pageNumber").description("Current page number in pageable object"));
        fields.add(fieldWithPath("pageable.pageSize").description("The size of the page in pageable object"));
        fields.add(fieldWithPath("pageable.offset").description("The offset of the current page"));
        fields.add(fieldWithPath("pageable.paged").description("Whether the result is paged"));
        fields.add(fieldWithPath("pageable.unpaged").description("Whether the result is unpaged"));
        fields.add(fieldWithPath("pageable.sort.empty").description("Whether the sort is empty"));
        fields.add(fieldWithPath("pageable.sort.sorted").description("Whether the sort is sorted"));
        fields.add(fieldWithPath("pageable.sort.unsorted").description("Whether the sort is unsorted"));

        return fields;
    }



}
