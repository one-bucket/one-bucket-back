package com.onebucket.domain.PushMessageManage.dto;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * <br>package name   : com.onebucket.domain.PushMessageManage.dto
 * <br>file name      : PushMessageDto
 * <br>date           : 11/26/24
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
public class PushMessageDto {

    @SuperBuilder
    @Getter
    public static class Base {
        private String title;
        private String body;
        private PushMessageType type;
        private String id;
    }

    @SuperBuilder
    @Getter
    public static class Tokens extends Base {
        private List<String> tokens;
    }

    @SuperBuilder
    @Getter
    public static class Token extends Base {
        private String token;
    }

    @SuperBuilder
    @Getter
    public static class Topic extends Base {
        private String topic;
    }
}
