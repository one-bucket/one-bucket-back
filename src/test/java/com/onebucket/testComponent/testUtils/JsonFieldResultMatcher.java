package com.onebucket.testComponent.testUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onebucket.global.exceptionManage.errorCode.ErrorCode;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <br>package name   : com.onebucket.testComponent
 * <br>file name      : JsonFieldResultMatcher
 * <br>date           : 2024-07-04
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
 * 2024-07-04        jack8              init create
 * </pre>
 */
public class JsonFieldResultMatcher implements ResultMatcher {
    private final List<ResultMatcher> matchers;

    public JsonFieldResultMatcher(List<ResultMatcher> matchers) {
        this.matchers = matchers;
    }

    @Override
    public void match(@NotNull MvcResult result) throws Exception {
        for (ResultMatcher matcher : matchers) {
            matcher.match(result);
        }
    }

    public static ResultMatcher hasStatus(ErrorCode errorCode) {
        return result -> {
            HttpStatus expectedStatus = errorCode.getHttpStatus();
            int actualStatus = result.getResponse().getStatus();
            Assertions.assertEquals(expectedStatus.value(), actualStatus,
                    "Expected status code " + expectedStatus.value() + " but got " + actualStatus);
        };
    }

    public static ResultMatcher hasKey(String key, String value) {
        List<ResultMatcher> matchers = new ArrayList<>();
        matchers.add(MockMvcResultMatchers.jsonPath("$." + key).value(value));
        return new JsonFieldResultMatcher(matchers);
    }

    public static ResultMatcher hasKey(String outputString) {
        return result -> Assertions.assertEquals(outputString, result.getResponse().getContentAsString());
    }

    public static ResultMatcher hasKey(ErrorCode errorCode) {
        List<ResultMatcher> matchers = new ArrayList<>();
        matchers.add(MockMvcResultMatchers.jsonPath("$.code").value(errorCode.getCode()));
        matchers.add(MockMvcResultMatchers.jsonPath("$.type").value(errorCode.getType()));
        matchers.add(MockMvcResultMatchers.jsonPath("$.message").value(errorCode.getMessage()));
        return new JsonFieldResultMatcher(matchers);
    }

    public static ResultMatcher hasKey(ErrorCode errorCode, String internalMessage) {
        List<ResultMatcher> matchers = new ArrayList<>();
        matchers.add(MockMvcResultMatchers.jsonPath("$.code").value(errorCode.getCode()));
        matchers.add(MockMvcResultMatchers.jsonPath("$.type").value(errorCode.getType()));
        matchers.add(MockMvcResultMatchers.jsonPath("$.message").value(errorCode.getMessage()));
        matchers.add(MockMvcResultMatchers.jsonPath("$.internalMessage").value(internalMessage));
        return new JsonFieldResultMatcher(matchers);
    }

    public static ResultMatcher hasKey(Object dto) throws IllegalAccessException {
        List<ResultMatcher> matchers = new ArrayList<>();
        Field[] fields = dto.getClass().getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            Object value = field.get(dto);
            if (value != null) {
                if (value instanceof LocalDate) {
                    DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
                    String formattedDate = ((LocalDate) value).format(formatter);
                    matchers.add(MockMvcResultMatchers.jsonPath("$." + field.getName()).value(formattedDate));
                } else if (value instanceof LocalDateTime) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
                    String formattedDateTime = ((LocalDateTime) value).truncatedTo(ChronoUnit.SECONDS).format(formatter);
                    matchers.add(MockMvcResultMatchers.jsonPath("$." + field.getName()).value(formattedDateTime));
                } else {
                    matchers.add(MockMvcResultMatchers.jsonPath("$." + field.getName()).value(value));
                }
            }
        }

        return new JsonFieldResultMatcher(matchers);
    }

    public static ResultMatcher hasKey(List<?> dtos) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        List<ResultMatcher> matchers = new ArrayList<>();

        // 리스트의 크기를 검증합니다.
        matchers.add(MockMvcResultMatchers.jsonPath("$.length()").value(dtos.size()));

        for (int i = 0; i < dtos.size(); i++) {
            Object dto = dtos.get(i);

            @SuppressWarnings("unchecked")
            Map<String, Object> map = mapper.convertValue(dto, Map.class);

            for (Map.Entry<String, Object> entry : map.entrySet()) {
                Object value = entry.getValue();

                if (value != null) {
                    if (value instanceof LocalDate) {
                        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
                        String formattedDate = ((LocalDate) value).format(formatter);
                        matchers.add(MockMvcResultMatchers.jsonPath("$[" + i + "]." + entry.getKey()).value(formattedDate));
                    } else if (value instanceof LocalDateTime) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
                        String formattedDateTime = ((LocalDateTime) value).truncatedTo(ChronoUnit.SECONDS).format(formatter);
                        matchers.add(MockMvcResultMatchers.jsonPath("$[" + i + "]." + entry.getKey()).value(formattedDateTime));
                    } else {
                        matchers.add(MockMvcResultMatchers.jsonPath("$[" + i + "]." + entry.getKey()).value(value));
                    }
                }
            }
        }

        return new JsonFieldResultMatcher(matchers);
    }

}
