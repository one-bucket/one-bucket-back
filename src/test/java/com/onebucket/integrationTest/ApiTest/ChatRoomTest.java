package com.onebucket.integrationTest.ApiTest;

import com.onebucket.domain.chatManage.dao.ChatRoomRepository;
import com.onebucket.domain.chatManage.domain.ChatMessage;
import com.onebucket.domain.chatManage.domain.ChatRoom;
import com.onebucket.domain.chatManage.dto.ChatMemberDto;
import com.onebucket.domain.chatManage.dto.CreateChatRoomDto;
import com.onebucket.domain.chatManage.service.ChatRoomService;
import com.onebucket.domain.memberManage.dao.MemberRepository;
import com.onebucket.domain.memberManage.domain.Member;
import com.onebucket.domain.memberManage.dto.CreateMemberRequestDto;
import com.onebucket.domain.memberManage.service.MemberService;
import com.onebucket.domain.memberManage.service.SignInService;
import com.onebucket.global.auth.jwtAuth.domain.JwtToken;
import com.onebucket.global.exceptionManage.customException.chatManageException.ChatManageException;
import com.onebucket.global.exceptionManage.customException.chatManageException.Exceptions.RoomNotFoundException;
import com.onebucket.global.exceptionManage.errorCode.ChatErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.*;

/**
 * <br>package name   : com.onebucket.integrationTest.ApiTest
 * <br>file name      : ChatRoomTest
 * <br>date           : 2024-08-16
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
 * 2024-08-16        SeungHoon              init create
 * </pre>
 */
@SpringBootTest
@TestPropertySource(properties = "de.flapdoodle.mongodb.embedded.version=4.16.2")
class ChatRoomTest{
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberService memberService;
    @Autowired
    private SignInService signInService;
    @Autowired
    private ChatRoomService chatRoomService;

    private List<JwtToken> tokens;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @BeforeEach
    void setUp() {
        memberRepository.deleteAll();
        chatRoomRepository.deleteAll();

        tokens = new ArrayList<>();

        for(int i=1;i<11;i++){
            CreateMemberRequestDto dto = CreateMemberRequestDto.builder()
                    .username("username" + i)
                    .password("password" + i)
                    .nickname("nickname" + i)
                    .build();
            memberService.createMember(dto);
            JwtToken token = signInService.signInByUsernameAndPassword("username" + i, "password" + i);
            tokens.add(token);
        }
    }

    @Test
    @DisplayName("채팅방 입장 성공 - 적절한 수의 유저 접근")
    void enterChatRoom_Concurrency_success() throws InterruptedException {
        Set<ChatMemberDto> memberSet = new HashSet<>();
        CreateChatRoomDto dto = CreateChatRoomDto.of("chatroom1", LocalDateTime.now(),"username1",memberSet);
        String roomId = chatRoomService.createChatRoom(dto);

        int numberOfThreads = 10;
        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch countDownLatch = new CountDownLatch(numberOfThreads);
        List<Member> members = memberRepository.findAll();
        for(int i=0;i<numberOfThreads;i++){
            int index = i;
            executor.submit(() -> {
                    chatRoomService.enterChatRoom(roomId,members.get(index).getUsername());
                    countDownLatch.countDown();
                }
            );
        }
        countDownLatch.await();
        executor.shutdown();

        ChatRoom chatRoom = chatRoomRepository.findByRoomId(roomId).orElseThrow(
                () -> new RoomNotFoundException(ChatErrorCode.NOT_EXIST_ROOM)
        );
        assertThat(chatRoom.getMembers().size()).isEqualTo(numberOfThreads);
    }

    @Test
    @DisplayName("채팅 저장 성공 - 모든 채팅 정상 저장")
    void addChatMessage_concurrency_success() throws InterruptedException {
        Set<ChatMemberDto> memberSet = new HashSet<>();
        CreateChatRoomDto dto = CreateChatRoomDto.of("chatroom1", LocalDateTime.now(),"username1",memberSet);
        String roomId = chatRoomService.createChatRoom(dto);

        int numberOfThreads = 10;
        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch countDownLatch = new CountDownLatch(numberOfThreads);
        for(int i=0;i<numberOfThreads;i++){
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setRoomId(roomId);
            executor.submit(() -> {
                try {
                    chatRoomService.addChatMessage(chatMessage);
                } catch (Exception e) {
                    // 예외 발생 시 로그 기록
                    e.printStackTrace();
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();
        executor.shutdown();
        ChatRoom chatRoom = chatRoomRepository.findByRoomId(roomId).orElseThrow(
                () -> new RoomNotFoundException(ChatErrorCode.NOT_EXIST_ROOM)
        );
        assertThat(chatRoom.getMessages().size()).isEqualTo(numberOfThreads);
    }
}
