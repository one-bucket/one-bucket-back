package com.onebucket.domain.chatManager.mongo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * <br>package name   : com.onebucket.domain.chatManager.mongo
 * <br>file name      : ChatMessage
 * <br>date           : 2024-10-17
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


@Document(collation = "{ locale: 'ko' }")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {

    @Id
    private String id;  // MongoDB가 자동으로 생성하는 _id 필드

    private String roomId;  // 채팅방 ID를 연결

    private String sender;
    private String message;

    @Indexed
    private Date timestamp;  // 타임스탬프에 인덱스를 추가해 검색 최적화
}