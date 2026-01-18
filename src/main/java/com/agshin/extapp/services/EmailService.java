package com.agshin.extapp.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    @Value("${app.frontend.password-reset-url}")
    private String passwordResetUrl;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendPasswordResetEmail(String to, String rawToken) {
        String resetLink = passwordResetUrl + "?token=" + rawToken;

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject("Reset Token Password");

            helper.setText(buildPasswordResetHtml(resetLink), true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * TODO: Build in template
     *
     * @return
     */
    private String buildPasswordResetHtml(String resetLink) {
        return """
                    <html>
                        <body>
                            <p>You requested a password reset.</p>
                            <p>
                                <a href="%s"> Click here to reset your password</a>
                            </p>
                            <p>This link expires in 1 hour.</p>
                        </body>
                    </html>
                """.formatted(resetLink);
    }
}
