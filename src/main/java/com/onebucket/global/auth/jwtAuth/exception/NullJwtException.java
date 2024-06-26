package com.onebucket.global.auth.jwtAuth.exception;

import io.jsonwebtoken.JwtException;

/**
 * <br>package name   : com.onebucket.global.auth.jwtAuth.exception
 * <br>file name      : NullJwtException
 * <br>date           : 2024-06-26
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
 * 2024-06-26        jack8              init create
 * </pre>
 */
public class NullJwtException extends JwtException {
    public NullJwtException(String message) {
        super(message);
    }
}
