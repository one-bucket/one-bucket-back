package com.onebucket.domain.memberManage.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onebucket.domain.memberManage.dto.CreateMemberRequestDto;
import com.onebucket.domain.memberManage.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * <br>package name   : com.onebucket.domain.memberManage.api
 * <br>file name      : RegisterControllerTest
 * <br>date           : 2024-06-27
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
 * 2024-06-27        jack8              init create
 * </pre>
 */

@ExtendWith(MockitoExtension.class)
class RegisterControllerTest {

    @Mock
    private MemberService memberService;

    @InjectMocks
    private RegisterController registerController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(registerController).build();
    }

    @Test
    @DisplayName("회원등록 테스트 성공")
    void testRegister_success() throws Exception {
        //given
        CreateMemberRequestDto dto = CreateMemberRequestDto.builder()
                .username("testuser")
                .password("password")
                .nickname("nickname")
                .build();

        //when & then
        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto)))
                        .andExpect(status().isOk())
                        .andExpect(result -> assertEquals("success create", result.getResponse().getContentAsString()));

        verify(memberService, times(1)).createMember(any(CreateMemberRequestDto.class));
    }

    @Test
    @DisplayName("회원 등록 실패 - 유효성 검사 실패")
    void testRegister_fail_invalidValue() throws Exception {
        //given
        CreateMemberRequestDto dto = CreateMemberRequestDto.builder()
                .username("")
                .password("password")
                .nickname("nickname")
                .build();

        //when & then
        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto)))
                        .andExpect(status().isBadRequest());

        verify(memberService, never()).createMember(any(CreateMemberRequestDto.class));
    }

    @Test
    @DisplayName("회원 등록 실패 - 이미 존재하는 회원")
    void testRegister_fail_duplicateMember() throws Exception {
        //given
        CreateMemberRequestDto dto = CreateMemberRequestDto.builder()
                .username("testuser")
                .password("password")
                .nickname("nickname")
                .build();

        doThrow(new DataIntegrityViolationException("username already exists"))
                .when(memberService).createMember(any(CreateMemberRequestDto.class));

        //when & then
        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(content().string("username already exists"));

        verify(memberService, times(1)).createMember(any(CreateMemberRequestDto.class));
    }

    @Test
    @DisplayName("회원 등록 실패 - 중복된 닉네임")
    void testRegister_fail_duplicateNickname() throws Exception {
        // given
        CreateMemberRequestDto dto = CreateMemberRequestDto.builder()
                .username("testuser")
                .password("password")
                .nickname("duplicatenickname")
                .build();

        doThrow(new DataIntegrityViolationException("Nickname already exists"))
                .when(memberService).createMember(any(CreateMemberRequestDto.class));

        // when & then
        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(content().string("Nickname already exists"));

        verify(memberService, times(1)).createMember(any(CreateMemberRequestDto.class));
    }

}