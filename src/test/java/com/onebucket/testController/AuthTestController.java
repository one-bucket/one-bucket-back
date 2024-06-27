package com.onebucket.testController;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <br>package name   : com.onebucket.testController
 * <br>file name      : AuthTestController
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
@RestController
public class AuthTestController {

    @GetMapping("/security-endpoint")
    public String securityEndpoint() {
        return "This is a security endpoint!";
    }

    @GetMapping("/test/url")
    public String testUrl() {
        return "This is a public endpoint";
    }
}
