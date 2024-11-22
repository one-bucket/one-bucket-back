package com.onebucket.global.auth.springSecurity;

import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * <br>package name   : com.onebucket.global.auth.springSecurity
 * <br>file name      : CustomAuthentication
 * <br>date           : 2024-11-08
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
@Getter
public class CustomAuthentication extends UsernamePasswordAuthenticationToken {

    private final Long userId;
    private final Long univId;
    public CustomAuthentication(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities, Long userId, Long univId) {
        super(principal, credentials, authorities);
        this.userId = userId;
        this.univId = univId;
    }
}
