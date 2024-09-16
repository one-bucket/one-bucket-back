package com.onebucket.domain.chatManager.repository;

import com.onebucket.domain.chatManager.dto.ChatRoom;
import org.springframework.stereotype.Repository;

import java.text.CollationElementIterator;
import java.util.*;

/**
 * <br>package name   : com.onebucket.domain.chatManager.repository
 * <br>file name      : ChatRepository
 * <br>date           : 2024-09-16
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

@Repository
public class ChatRepository {
    private Map<String, ChatRoom> chatRoomMap = new LinkedHashMap<>();

    public List<ChatRoom> findAllRoom() {
        List chatRooms = new ArrayList<>(chatRoomMap.values());
        Collections.reverse(chatRooms);
        return chatRooms;
    }

    public ChatRoom findByRoomId(String roomId) {
        return chatRoomMap.get(roomId);
    }

    public ChatRoom createChatRoom(String roomName) {
        ChatRoom chatRoom = new ChatRoom().create(roomName);
        chatRoomMap.put(chatRoom.getRoomId(), chatRoom);
        System.out.println(chatRoomMap);

        return chatRoom;
    }

    public void plusUserCnt(String roomId) {
        ChatRoom room = chatRoomMap.get(roomId);
        room.setUserCount(room.getUserCount() + 1);
    }

    public void minusUserCnt(String roomId) {
        ChatRoom room = chatRoomMap.get(roomId);
        room.setUserCount(room.getUserCount() - 1);
    }

    public String addUser(String roomId, String username) {
        ChatRoom room = chatRoomMap.get(roomId);
        String userUUID = UUID.randomUUID().toString();

        room.getUserList().put(userUUID, username);

        return userUUID;
    }

    public void delUser(String roomId, String userUUID) {
        ChatRoom room = chatRoomMap.get(roomId);
        room.getUserList().remove(userUUID);
    }

    public String getUserName(String roomId, String userUUID) {
        ChatRoom room = chatRoomMap.get(roomId);
        return room.getUserList().get(userUUID);
    }

    public ArrayList<String> getUserList(String roomId) {
        ArrayList<String> list = new ArrayList<>();
        ChatRoom room = chatRoomMap.get(roomId);
        room.getUserList().forEach((key, value) -> list.add(value));
        return list;
    }
}
