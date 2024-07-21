package com.onebucket.global.exceptionManage.exceptionHandler;

import com.onebucket.global.exceptionManage.ErrorResponse;
import com.onebucket.global.exceptionManage.customException.memberManageExceptoin.AuthenticationException;
import com.onebucket.global.exceptionManage.errorCode.ErrorCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * <br>package name   : com.onebucket.global.exceptionManage.exceptionHandler
 * <br>file name      : AuthenticationExceptionHandlerTest
 * <br>date           : 2024-06-28
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
 * 2024-06-28        jack8              init create
 * </pre>
 */

@ExtendWith(MockitoExtension.class)
class BaseExceptionHandlerTest {

    @InjectMocks
    private BaseExceptionHandler baseExceptionHandler;

    @Test
    public void testHandleRegisterException() {
        AuthenticationException authenticationException = mock(AuthenticationException.class);
        ErrorCode errorCode = mock(ErrorCode.class);

        when(authenticationException.getErrorCode()).thenReturn(errorCode);
        when(errorCode.getHttpStatus()).thenReturn(HttpStatus.CONFLICT);
        when(authenticationException.getMessage()).thenReturn("test exception occur");

        ResponseEntity<ErrorResponse> response =
                baseExceptionHandler.handleException(authenticationException);

        assertNotNull(response);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(errorCode.getCode(), Objects.requireNonNull(response.getBody()).getCode());
        assertEquals("test exception occur", response.getBody().getInternalMessage());
    }

}