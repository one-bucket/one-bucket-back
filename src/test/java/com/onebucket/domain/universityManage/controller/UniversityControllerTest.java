package com.onebucket.domain.universityManage.controller;

import com.google.gson.Gson;
import com.onebucket.domain.universityManage.service.UniversityService;
import com.onebucket.global.exceptionManage.exceptionHandler.UniversityExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;

/**
 * <br>package name   : com.onebucket.domain.universityManage.controller
 * <br>file name      : UniversityControllerTest
 * <br>date           : 2024-07-05
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
 * =======================================================
 * DATE           AUTHOR               NOTE
 * -------------------------------------------------------
 * 2024-07-05        SeungHoon              init create
 * </pre>
 */
@ExtendWith(MockitoExtension.class)
class UniversityControllerTest {
    @InjectMocks
    private UniversityController universityController;

    @Mock
    private UniversityService universityService;

    private MockMvc mockMvc;
    private Gson gson;

    @BeforeEach
    void init(){
        gson = new Gson();
        mockMvc = MockMvcBuilders.standaloneSetup(universityController)
                .setControllerAdvice(new UniversityExceptionHandler())
                .build();
    }
}