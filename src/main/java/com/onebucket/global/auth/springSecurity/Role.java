package com.onebucket.global.auth.springSecurity;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <br>package name   : com.onebucket.global.auth.springSecurity
 * <br>file name      : Role
 * <br>date           : 2024-10-16
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
 * 2024-10-16        SeungHoon              init create
 * </pre>
 */
@Getter
@AllArgsConstructor
public enum Role {
    USER("ROLE_USER"),
    GUEST("ROLE_GUEST"),
    ADMIN("ROLE_ADMIN")
    ;

    private final String role;
}
