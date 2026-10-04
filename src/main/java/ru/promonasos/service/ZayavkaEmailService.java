package ru.promonasos.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.promonasos.model.Zayavka;

import java.time.OffsetDateTime;

@Service
public class ZayavkaEmailService {

    private final JavaMailSender mailSender;
    private final String emailTo;
    private final String emailFrom;

    public ZayavkaEmailService(JavaMailSender mailSender,
                               @Value("${app.inquiry.email-to}") String emailTo,
                               @Value("${app.inquiry.email-from}") String emailFrom) {
        this.mailSender = mailSender;
        this.emailTo = emailTo;
        this.emailFrom = emailFrom;
    }

    public void otpravit(Zayavka zayavka, String stranitsa) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailFrom);
        message.setTo(emailTo);
        message.setSubject("Новая заявка с сайта hermetica.su");
        message.setText("Новая заявка с сайта hermetica.su\n\n"
                + "Имя: " + bezNulla(zayavka.getImya()) + "\n"
                + "Телефон или почта: " + bezNulla(zayavka.getKontakt()) + "\n"
                + "Организация: " + bezNulla(zayavka.getOrganizatsiya()) + "\n"
                + "Запрос: " + bezNulla(zayavka.getZadacha()) + "\n"
                + "Страница отправки: " + stranitsa + "\n"
                + "Время получения: " + OffsetDateTime.now());
        mailSender.send(message);
    }

    private String bezNulla(String znachenie) {
        return znachenie == null || znachenie.isBlank() ? "не указано" : znachenie.strip();
    }
}
