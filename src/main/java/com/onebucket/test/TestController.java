package com.onebucket.test;


import com.onebucket.domain.memberManage.service.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final SecurityUtils securityUtils;

    @GetMapping("/test/hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("/member-info")
    public String memberInfo() {
        return securityUtils.getCurrentUsername();
    }
}
