package com.onebucket.domain.chatManager.service;

import com.onebucket.domain.chatManager.dto.ChatRoomDto;
import com.onebucket.domain.chatManager.entity.ChatRoomMemberId;
import com.onebucket.domain.chatManager.mongo.ChatMessage;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <br>package name   : com.onebucket.domain.chatManager.service
 * <br>file name      : ChatRoomService
 * <br>date           : 10/16/24
 * <pre>
 * <span style="color: white;">[description]</span>
 * {@link ChatRoomServiceImpl} 에 대한 인터페이스. 채팅방에 대한 기본적인 서비스 레이어 메서드가 존재한다.
 * ChatRoom에 대한 CRUD 알고리즘을 비롯하여 유저 리스트 반환 및 유저 개인이 속해있는 채팅방 리스트 또한 반환한다.
 * </pre>
 * <pre>
 * <span style="color: white;">usage:</span>
 * {@code
 *
 * } </pre>
 */
public interface ChatRoomService {


    /**
     * {@code roomId}를 받아, 해당 아이디가 이미 존재하는지 여부를 반환하는 메서드.
     * 데이터베이스에서 직접 조회하여 반환한다. 일반적으로 유저가 해당 채팅방을 구독하기 위해 소켓 명령어를 전송할 때
     * 해당 채팅방이 존재하는지에 대한 여부를 파악한다.
     * @param roomId 해당 아이디가 데이터베이스에 존재하는지 확인한다.
     * @return 존재하면 true, 아니면 false를 반환한다.
     */
    boolean existsById(String roomId);

    /**
     * 유저의 정보화 채팅방 아이디를 받아, 해당 유저가 해당 채팅방에 속해있는지에 대한 인가를 위해
     * 존재하는 서비스 레이어 메서드이다. 매개변수로 유저와 채팅방 아이디를 받아 데이터베이스에서 검색한다.
     * TODO : 어쩌면 캐싱을 해야 할지도? 고민 필요
     * @param dto 유저의 id와 채팅방 id가 포함되어 있다.
     * @return 해당 유저가 채팅방의 일원이라면 true를 아니면 false를 반환한다.
     */
    boolean isMemberOfChatRoom(ChatRoomDto.ManageMember dto);


    /**
     * 입력받은 채팅방 id에 대하여 속해있는 유저의 리스트를 반환한다. 기본적으로 해당 정보는 채팅방에 대한
     * 정보를 불러올 때 같이 넘어가지만, 유저의 리스트만 개별적으로 필요한 경우에 사용된다.
     * @param roomId 채팅방의 id
     * @return 유저의 닉네임, 가입 일시 및 role이 포함되어 있다.
     */
    List<ChatRoomDto.MemberInfo> getMemberList(String roomId);

    /**
     * 채팅방을 생성하는 메서드. UUID를 이용해 채팅방 아이디를 생성하고, 처음 채팅방을 생성하는 유저의 정보를
     * 유저 리스트에 포함시킨다. <br>
     * pending trade 즉, 기본적으로 채팅방은 거래 게시판에서 생성되고, 채팅방에서 해당 거래에 대한 정보를
     * 조회할 수 있도록 하기 위해 pending trade를 설정하여 준다. 이 역시 매개변수에 포함되어 있다. 다만, 현재
     * 채팅방 구현을 위해서 테스트 중이므로 해당 부분은 주석처리 되어 있다.
     * @param dto 채팅방의 이름 및 유저id, 거래id가 포함되어 있다.
     * @return 생성된 채팅방의 id가 반환된다.
     */
    String createRoom(ChatRoomDto.CreateRoom dto);

    ChatRoomDto.GetTradeInfo getTradeInfo(String roomId);

    void changeRoomName(ChatRoomDto.ChangeRoomName dto);

    void deleteRoom(String roomId);

    ChatRoomDto.ChatRoomInfo getRoomInfo(ChatRoomDto.InfoAfterTime dto);

    List<String> getRoomIds(Long userId);

    void setDisconnectTime(ChatRoomDto.SetDisconnectTime dto);

    Long addMember(ChatRoomDto.ManageMember dto);

    Long quitMember(ChatRoomDto.ManageMember dto);

    LocalDateTime getDisconnectTime(ChatRoomMemberId id);
    List<ChatMessage> getMessageAfterTimestamp(ChatRoomDto.InfoAfterTime dto);
}
