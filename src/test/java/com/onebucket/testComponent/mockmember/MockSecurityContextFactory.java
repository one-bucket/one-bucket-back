package com.onebucket.testComponent.mockmember;

import com.onebucket.domain.memberManage.domain.Member;
import com.onebucket.global.auth.springSecurity.CustomUserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Arrays;
import java.util.List;

/**
 * <br>package name   : com.onebucket.testComponent.mockmember
 * <br>file name      : MockSecurityContextFactory
 * <br>date           : 2024-06-30
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
 * 2024-06-30        jack8              init create
 * </pre>
 */
public class MockSecurityContextFactory implements WithSecurityContextFactory<MockMember> {
    @Override
    public SecurityContext createSecurityContext(MockMember annotation) {

        Member mockMember = Member.builder()
                .id(annotation.id())
                .username(annotation.username())
                .password(annotation.password())
                .nickname(annotation.nickname())
                .build();

        CustomUserDetails customUserDetails = new CustomUserDetails(mockMember);
        List<SimpleGrantedAuthority> authorities = Arrays.stream(annotation.roles())
                .map(SimpleGrantedAuthority::new)
                .toList();

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                customUserDetails, annotation.password(), authorities);

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(token);

        return context;
    }
}
