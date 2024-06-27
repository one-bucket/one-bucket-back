package com.onebucket.global.auth.springSecurity;

import com.onebucket.domain.memberManage.domain.Member;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * <br>package name   : com.onebucket.global.auth.springSecurity
 * <br>file name      : CustomUserDetails
 * <br>date           : 2024-06-27
 * <pre>
 * <span style="color: white;">[description]</span>
 * CustomUserDetails implements UserDetails, which from {@link Member}.
 * Used in spring security to insert user info to SecurityContextHolder and load it.
 *
 * All information locates in Member entity, also saved in MYSQL database. Init value of
 * boolean valuable is true, and authority is "GUEST"
 * </pre>
 * <pre>
 * <span style="color: white;">usage:</span>
 * {@code
 * NOT -- USED
 * } </pre>
 * <pre>
 * modified log :
 * =======================================================
 * DATE           AUTHOR               NOTE
 * -------------------------------------------------------
 * 2024-06-27        jack8              init create
 * </pre>
 */

@Builder
@Getter
public class CustomUserDetails implements UserDetails {

    private Member member;

    public CustomUserDetails(Member member) {
        this.member = member;
    }
    @Override
    public boolean isAccountNonExpired() {
        return member.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return member.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return member.isAccountNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return member.isEnable();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return member.getRoles().stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getUsername();
    }
}
