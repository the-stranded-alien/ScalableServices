package in.hotel.notification_service.service;

import in.hotel.notification_service.model.Email;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(to);
            mailMessage.setSubject(subject);
            mailMessage.setText(body);
            mailSender.send(mailMessage);
            log.info("Email sent successfully to {} with subject {}", to, subject);
        } catch (Exception e) {
            log.error("Error sending email to {} with subject {}", to, subject, e);
        }
    }

    public void sendEmail(Email email) {
        sendEmail(email.getTo(), email.getSubject(), email.getBody());
    }
}
