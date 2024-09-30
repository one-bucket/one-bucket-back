package com.onebucket.domain.mailManage.service;

import com.onebucket.domain.mailManage.dto.EmailMessage;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Map;

/**
 * <br>package name   : com.onebucket.domain.mailManage.service
 * <br>file name      : MailServiceImpl
 * <br>date           : 2024-09-20
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
 * 2024-09-20        SeungHoon              init create
 * </pre>
 */
@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    // 이메일 발송 메서드, 템플릿 선택을 포함
    public void sendEmail(EmailMessage emailMessage, String templateName, Map<String, Object> variables) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(emailMessage.to());
            mimeMessageHelper.setSubject(emailMessage.title());

            // Thymeleaf context에 데이터를 넣기
            Context context = new Context();
            context.setVariables(variables); // 변수를 설정

            // 선택된 템플릿 이름으로 템플릿을 처리
            String htmlContent = templateEngine.process(templateName, context);
            mimeMessageHelper.setText(htmlContent, true);

            // 이메일 발송
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
