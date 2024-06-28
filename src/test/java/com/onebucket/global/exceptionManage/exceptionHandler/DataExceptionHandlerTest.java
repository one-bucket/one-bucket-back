package com.onebucket.global.exceptionManage.exceptionHandler;

import com.onebucket.global.exceptionManage.ErrorResponse;
import com.onebucket.global.exceptionManage.errorCode.ValidateErrorCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * <br>package name   : com.onebucket.global.exceptionManage.exceptionHandler
 * <br>file name      : DataExceptionHandlerTest
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
class DataExceptionHandlerTest {

    @InjectMocks
    private DataExceptionHandler dataExceptionHandler;


    @Test
    public void testDataExceptionHandler() {
        //given
        BindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "object name");

        bindingResult.addError(new FieldError("objectName", "fieldName", "defaultMessage"));
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(null, bindingResult);

        ResponseEntity<ErrorResponse> response = dataExceptionHandler.handleValidationExceptions(exception);


        assertEquals(HttpStatus.BAD_REQUEST,response.getStatusCode());
        assertEquals(ValidateErrorCode.INVALID_DATA.getCode(), Objects.requireNonNull(response.getBody()).getCode());
        assertEquals("fieldName: defaultMessage", response.getBody().getInternalMessage());


    }

}