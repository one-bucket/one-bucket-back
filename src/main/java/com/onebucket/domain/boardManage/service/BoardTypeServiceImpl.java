package com.onebucket.domain.boardManage.service;

import com.onebucket.domain.boardManage.dao.BoardTypeRepository;
import com.onebucket.domain.boardManage.entity.BoardType;
import com.onebucket.global.exceptionManage.customException.boardManageException.AdminManageBoardException;
import com.onebucket.global.exceptionManage.errorCode.BoardErrorCode;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.util.test.FixedSecureRandom;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

/**
 * <br>package name   : com.onebucket.domain.boardManage.service
 * <br>file name      : BoardTypeServiceImpl
 * <br>date           : 2024-07-18
 * <pre>
 * <span style="color: white;">[description]</span>
 *
 * </pre>
 * <pre>
 * <span style="color: white;">usage:</span>
 * {@code
 *
 * } </pre>
 * <pre>
 * modified log :
 * ====================================================
 * DATE           AUTHOR               NOTE
 * ----------------------------------------------------
 * 2024-07-18        jack8              init create
 * </pre>
 */

@Service
@RequiredArgsConstructor
public class BoardTypeServiceImpl implements BoardTypeService {

    private final BoardTypeRepository boardTypeRepository;

    @Override
    public void createBoardType(String name, String description) {
        BoardType boardType = BoardType.builder()
                .name(name)
                .description(description)
                .build();

        try {
            boardTypeRepository.save(boardType);
        } catch(DataIntegrityViolationException e) {
            throw new AdminManageBoardException(BoardErrorCode.DUPLICATE_BOARD_TYPE);
        }
    }
}
