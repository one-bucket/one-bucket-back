package com.onebucket.domain.chatManage;

/**
 * <br>package name   : com.onebucket.domain.chatManage
 * <br>file name      : Const
 * <br>date           : 2024-07-08
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
 * 2024-07-08        SeungHoon              init create
 * </pre>
 */
public final class Const {
    // 메세지 관련 상수
    public static final String CHAT_ROOMS = "CHAT_ROOM";
    public static final String CHAT_MESSAGE_ORIGIN = "CHAT_ROOM_";
    public static final String LOG_DIRECTORY = "logs/chat_logs/";

    // 생성자 private 선언해서 인스턴스화 방지
    private Const() {
        throw new AssertionError("Cannot instantiate constants class");
    }
}
