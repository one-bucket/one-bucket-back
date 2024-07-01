package com.onebucket.global.exceptionManage.exceptionHandler;

import com.onebucket.global.exceptionManage.ErrorResponse;
import com.onebucket.global.exceptionManage.customException.memberManageExceptoin.RegisterException;
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
class AuthenticationExceptionHandlerTest {

    @InjectMocks
    private AuthenticationExceptionHandler authenticationExceptionHandler;

    @Test
    public void testHandleRegisterException() {
        RegisterException registerException = mock(RegisterException.class);
        ErrorCode errorCode = mock(ErrorCode.class);

        when(registerException.getErrorCode()).thenReturn(errorCode);
        when(errorCode.getHttpStatus()).thenReturn(HttpStatus.CONFLICT);
        when(registerException.getMessage()).thenReturn("test exception occur");

        ResponseEntity<ErrorResponse> response =
                authenticationExceptionHandler.handleRegisterException(registerException);

        assertNotNull(response);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(errorCode.getCode(), Objects.requireNonNull(response.getBody()).getCode());
        assertEquals("test exception occur", response.getBody().getInternalMessage());
    }

}