package com.onebucket.global.utils;

import java.util.function.Consumer;

/**
 * <br>package name   : com.onebucket.global.utils
 * <br>file name      : EntityUtils
 * <br>date           : 2024-07-03
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
 * 2024-07-03        jack8              init create
 * </pre>
 */
public class EntityUtils {

    public static <T> void updateIfNotNull(T value, Consumer<T> consumer) {
        if (value != null) {
            consumer.accept(value);
        }
    }
}
