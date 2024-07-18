package com.onebucket.domain.boardManage.dto;

import com.onebucket.domain.boardManage.entity.BoardType;
import com.onebucket.domain.universityManage.domain.University;
import com.onebucket.global.exceptionManage.errorCode.UniversityErrorCode;
import lombok.Builder;
import lombok.Getter;

/**
 * <br>package name   : com.onebucket.domain.boardManage.dto
 * <br>file name      : CreateBoardDto
 * <br>date           : 2024-07-18
 * <pre>
 * <span style="color: white;">[description]</span>
 * {@link com.onebucket.domain.boardManage.service.BoardServiceImpl BoardService} 의 createBoard 메서드의 메개변수
 * </pre>
 */
@Builder
@Getter
public class CreateBoardDto {

    private University university;
    private BoardType boardType;
    private String name;
    private String description;
}
