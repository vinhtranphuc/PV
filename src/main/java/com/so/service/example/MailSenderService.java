package com.so.service.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import com.so.common.FileUtils;
import com.so.common.MailContentBuilderService;

import javax.annotation.PostConstruct;
import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Service
public class MailSenderService {

    @Autowired
    private MailContentBuilderService mailContentBuilder;

    @Autowired
    private FileUtils fileUtils;

    @Value("${mail.smtp.host}")
    private String mailSmtpHost;
    @Value("${mail.smtp.port}")
    private String mailSmtpPort;
    @Value("${mail.smtp.auth}")
    private boolean mailSmtpAuth;
    @Value("${mail.user}")
    private String mailUser;
    @Value("${mail.password}")
    private String mailPassword;

    public MimeMessage mimeMessage;

    @PostConstruct
    private void createMimeMessage() throws AddressException, MessagingException {

        Properties props = new Properties();
        props.put("mail.smtp.host", mailSmtpHost);
        props.put("mail.smtp.port", mailSmtpPort);
        props.put("mail.smtp.auth", mailSmtpAuth);

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(mailUser, mailPassword);
            }
        });

        this.mimeMessage = new MimeMessage(session);
        mimeMessage.setFrom(new InternetAddress(mailUser));
    }

    public void sendContactInfo(String email,String title,String[] contentLines) throws MessagingException, IOException {

        mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        messageHelper.setSubject(title);

        Map<String, Object> details = new HashMap<String, Object>();
        details.put("title", title);
        details.put("contentLines", contentLines);
        String content = mailContentBuilder.build("Mail/contact-template.html", details);
        messageHelper.setText(content, true);
        messageHelper.addInline("logoEmail", new ByteArrayResource(getLogoEmail()), "image/png");

        Transport.send(mimeMessage);
    }

    private byte[] getLogoEmail() throws MessagingException, IOException {
        Resource resource = fileUtils.getCommonResourceByPath("", "logo-email.png");
        return FileCopyUtils.copyToByteArray(resource.getInputStream());
    }
}
