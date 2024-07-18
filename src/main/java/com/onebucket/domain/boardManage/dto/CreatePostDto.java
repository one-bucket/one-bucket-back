package com.onebucket.domain.boardManage.dto;

import com.onebucket.domain.boardManage.entity.Board;
import com.onebucket.domain.memberManage.domain.Member;
import lombok.Builder;
import lombok.Getter;

/**
 * <br>package name   : com.onebucket.domain.boardManage.dto
 * <br>file name      : CreatePostDto
 * <br>date           : 2024-07-18
 * <pre>
 * <span style="color: white;">[description]</span>
 * {@link com.onebucket.domain.boardManage.service.PostServiceImpl PostService} 의 createPost 메서드 매개변수
 * </pre>
 */
@Getter
@Builder
public class CreatePostDto {

    private Board board;
    private Member author;
    private String title;
    private String text;

}
