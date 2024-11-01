package com.onebucket.global.utils;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * <br>package name   : com.onebucket.global.utils
 * <br>file name      : ImageUtils
 * <br>date           : 10/30/24
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
@Component
public class ImageUtils {
    List<String> supportImageFormat = new ArrayList<>(List.of(
            "png",
            "jpg",
            "gif"
    ));


    public String getFileExtensionFromMessage(String base64) {
        // "data:image/png;base64,z"와 같은 접두사가 포함된 경우 이를 분리
        String[] parts = base64.split(",");
        if (parts.length > 1) {
            String mimeType = parts[0]; // "data:image/png;base64" 부분

            String format =  mimeType.split("/")[1].split(";")[0];
            if(!supportImageFormat.contains(format)) {
                return format;
            } else {
                throw new IllegalArgumentException("Invalid Image format");
            }
        } else {
            throw new IllegalArgumentException("Invalid Base64 format");
        }
    }

    public String getFileNameFromMessage(String message) {
        int startIndex = message.indexOf("[") + 1;
        int endIndex = message.indexOf("]");
        if (startIndex > 0 && endIndex > startIndex) {
            return message.substring(startIndex, endIndex); // "filename.png"
        } else {
            throw new IllegalArgumentException("Invalid format. No filename found.");
        }
    }

    public String getBase64FromMessage(String message) {
        // 파일 이름 제거 (예: "[image-name].png")
        int base64StartIdx = message.indexOf("]") + 1;

        if (base64StartIdx <= 0 || base64StartIdx >= message.length()) {
            throw new IllegalArgumentException("Invalid format: No valid Base64 data found.");
        }

        // MIME 타입 포함한 Base64 데이터 반환
        return message.substring(base64StartIdx);
    }

}
