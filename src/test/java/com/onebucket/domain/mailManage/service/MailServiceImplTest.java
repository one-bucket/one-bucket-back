//package com.onebucket.domain.mailManage.service;
//
//import com.onebucket.domain.mailManage.dto.EmailMessage;
//import jakarta.mail.MessagingException;
//import jakarta.mail.internet.MimeMessage;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Spy;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessageHelper;
//import org.thymeleaf.context.Context;
//import org.thymeleaf.spring6.SpringTemplateEngine;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
///**
// * <br>package name   : com.onebucket.domain.mailManage.service
// * <br>file name      : MailServiceImplTest
// * <br>date           : 2024-09-28
// * <pre>
// * <span style="color: white;">[description]</span>
// *
// * </pre>
// * <pre>
// * <span style="color: white;">usage:</span>
// * {@code
// *
// * } </pre>
// * <pre>
// * modified log :
// * =======================================================
// * DATE           AUTHOR               NOTE
// * -------------------------------------------------------
// * 2024-09-28        SeungHoon              init create
// * </pre>
// */
//@ExtendWith(MockitoExtension.class)
//class MailServiceImplTest {
//
//    @Mock
//    private JavaMailSender mailSender;
//
//    @Mock
//    private SpringTemplateEngine templateEngine;
//
//    @Mock
//    private MimeMessage mimeMessage;
//
//    @InjectMocks
//    private MailServiceImpl mailService;
//
//    @Test
//    void sendEmail_success() {
//        EmailMessage emailMessage = EmailMessage.of("user1","test mail");
//        String template = "testTemplate";
//        Map<String, Object> variables = new ConcurrentHashMap<>();
//        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
//        // templateEngine.process() 부분을 when().thenReturn()로 수정
//        when(templateEngine.process(anyString(), any(Context.class)))
//                .thenReturn("<h1>Test HTML Content</h1>");
//        mailService.sendEmail(emailMessage, template, variables);
//        // then
//        verify(mailSender).send(mimeMessage);  // JavaMailSender의 send 호출 여부 검증
//    }
//}