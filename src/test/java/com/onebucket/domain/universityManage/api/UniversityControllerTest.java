package com.onebucket.domain.universityManage.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.onebucket.domain.universityManage.domain.University;
import com.onebucket.domain.universityManage.dto.UniversityDto;
import com.onebucket.domain.universityManage.dto.UpdateUniversityDto;
import com.onebucket.domain.universityManage.service.UniversityService;
import com.onebucket.global.exceptionManage.customException.universityManageException.UniversityException;
import com.onebucket.global.exceptionManage.errorCode.UniversityErrorCode;
import com.onebucket.global.exceptionManage.exceptionHandler.BaseExceptionHandler;
import com.onebucket.global.exceptionManage.exceptionHandler.DataExceptionHandler;
import com.onebucket.global.exceptionManage.exceptionHandler.UniversityExceptionHandler;
import com.onebucket.global.utils.SuccessResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.IntStream;

import static com.onebucket.testComponent.testUtils.JsonFieldResultMatcher.hasKey;
import static com.onebucket.testComponent.testUtils.JsonFieldResultMatcher.hasStatus;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


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

    @Mock
    private UniversityService universityService;

    @InjectMocks
    private UniversityController universityController;


    private MockMvc mockMvc;

    private ObjectMapper objectMapper;


    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(universityController)
                .setControllerAdvice(new BaseExceptionHandler(), new DataExceptionHandler(), new UniversityExceptionHandler())
                .build();
    }

    //-+-+-+-+-+-+]] createUniversity test [[-+-+-+-+-+-+
    @Test
    @DisplayName("createUniversity - success")
    void testCreateUniversity_success() throws Exception {
        UniversityDto dto = getDto();
        when(universityService.createUniversity(any(UniversityDto.class))).thenReturn(1L);

        mockMvc.perform(post("/admin/univs")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(hasKey(new SuccessResponseDto("success create university / id is 1")));
    }

    @Test
    @DisplayName("createUniversity - fail / duplicate university")
    void testCreateUniversity_fail_duplicateUniv() throws Exception {
        UniversityDto dto = getDto();

        UniversityErrorCode code = UniversityErrorCode.DUPLICATE_UNIVERSITY;
        UniversityException exception = new UniversityException(code);

        when(universityService.createUniversity(any(UniversityDto.class))).thenThrow(exception);

        mockMvc.perform(post("/admin/univs")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(hasStatus(code))
                .andExpect(hasKey(code));

    }
    
    //-+-+-+-+-+-+]] getAllUniversity test [[-+-+-+-+-+-+
    @Test
    @DisplayName("getAllUniversity - success")
    void testGetAllUniversity_success() throws Exception {

        List<UniversityDto> responses = IntStream.rangeClosed(1, 10)
                .mapToObj(i -> getDto("name" + i))
                .toList();



        when(universityService.findAllUniversity()).thenReturn(responses);

        mockMvc.perform(get("/admin/univs")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(hasKey(responses));
    }

    @Test
    @DisplayName("GetAllUniversity - success / blank DB")
    void testGetAllUniversity_success_blankDB() throws Exception {
        UniversityDto defaultDto = UniversityDto.builder()
                .name("not insert")
                .address("data")
                .email("yet")
                .build();

        when(universityService.findAllUniversity()).thenReturn(List.of(defaultDto));


        mockMvc.perform(get("/admin/univs")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(hasKey(List.of(defaultDto)));

    }

    //-+-+-+-+-+-+]] getUniversity test [[-+-+-+-+-+-+
    @Test
    @DisplayName("GetUniversity - success")
    void testGetUniversity_success() throws Exception {

        String name = "name";
        UniversityDto dto = getDto(name);
        when(universityService.getUniversity(name)).thenReturn(dto);

        mockMvc.perform(get("/admin/univs/{name}", name)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(hasKey(dto));
    }

    @Test
    @DisplayName("GetUniversity - fail / unknown university")
    void testGetUniversity_fail_unknownUniv() throws Exception {

        UniversityErrorCode code = UniversityErrorCode.NOT_EXIST_UNIVERSITY;
        UniversityException exception = new UniversityException(code);

        when(universityService.getUniversity(anyString())).thenThrow(exception);

        mockMvc.perform(get("/admin/univs/{name}", "name")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(hasStatus(code))
                .andExpect(hasKey(code));
    }

    //-+-+-+-+-+-+]] updateUniversity test [[-+-+-+-+-+-+

    @Test
    @DisplayName("updateUniversity - success")
    void testUpdateUniversity_success() throws Exception {
        String name = "name";
        UpdateUniversityDto dto = UpdateUniversityDto.builder()
                .address("new address")
                .build();

        mockMvc.perform(patch("/admin/univs/{name}", name)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(hasKey(new SuccessResponseDto("success update university")));

        verify(universityService, times(1)).updateUniversity(eq(name), any(UpdateUniversityDto.class));
    }

    @Test
    @DisplayName("updateUniversity - fail / unknown university")
    void testUpdateUniversity_fail_unknownUniv() throws Exception {
        String name = "name";
        UpdateUniversityDto dto = UpdateUniversityDto.builder()
                .address("new address")
                .build();

        UniversityErrorCode code = UniversityErrorCode.NOT_EXIST_UNIVERSITY;
        UniversityException exception = new UniversityException(code);

        doThrow(exception).when(universityService).updateUniversity(eq(name), any(UpdateUniversityDto.class));

        mockMvc.perform(patch("/admin/univs/{name}", name)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(hasStatus(code))
                .andExpect(hasKey(code));
    }








    private UniversityDto getDto(String name, String address, String email) {
        return UniversityDto.builder()
                .name(name)
                .address(address)
                .email(email)
                .build();
    }

    private UniversityDto getDto(String name) {
        return getDto(name, "address", "email@email.com");
    }

    private UniversityDto getDto() {
        return getDto("name");
    }
}