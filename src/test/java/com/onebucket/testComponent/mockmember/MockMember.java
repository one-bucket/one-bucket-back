package com.onebucket.testComponent.mockmember;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <br>package name   : com.onebucket.testComponent
 * <br>file name      : MockMember
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

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = MockSecurityContextFactory.class)
public @interface MockMember {

    long id() default 1L;
    String username() default "test user";
    String password() default "password";
    String nickname() default "nickname";
    String[] roles() default {"ROLE_USER"};
}
