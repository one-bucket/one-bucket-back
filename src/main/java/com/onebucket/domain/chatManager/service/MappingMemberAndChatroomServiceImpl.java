package com.onebucket.domain.chatManager.service;

import com.onebucket.domain.chatManager.dao.MappingMemberAndChatroomRepository;
import com.onebucket.domain.chatManager.dto.ChatRoomDto;
import com.onebucket.domain.chatManager.entity.MappingMemberAndChatroom;
import com.onebucket.domain.chatManager.entity.MappingMemberAndChatroomId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * <br>package name   : com.onebucket.domain.chatManager.service
 * <br>file name      : MappingMemberAndChatroomService
 * <br>date           : 2024-11-20
 * <pre>
 * <span style="color: white;">[description]</span>
 *
 * </pre>
 * <pre>
 * <span style="color: white;">usage:</span>
 * {@code
 *
 * } </pre>
 */

@Service
@RequiredArgsConstructor
public class MappingMemberAndChatroomServiceImpl implements MappingMemberAndChatroomService{
    private final MappingMemberAndChatroomRepository mappingMemberAndChatroomRepository;

    @Override
    public String getExistChatroom(ChatRoomDto.SearchMapper dto) {
        MappingMemberAndChatroomId mappingMemberAndChatroomId = MappingMemberAndChatroomId.builder()
                .member(dto.getUserId())
                .trade(dto.getTradeId())
                .build();

        return mappingMemberAndChatroomRepository.findById(mappingMemberAndChatroomId)
                .map(MappingMemberAndChatroom::getChatroom)
                .orElse(null);
    }
    @Override
    public void saveChatRoomMapping(ChatRoomDto.SaveMapper dto) {

        MappingMemberAndChatroom mappingMemberAndChatroom = MappingMemberAndChatroom.builder()
                .member(dto.getUserId())
                .trade(dto.getTradeId())
                .chatroom(dto.getRoomId())
                .build();

        mappingMemberAndChatroomRepository.save(mappingMemberAndChatroom);
    }
}