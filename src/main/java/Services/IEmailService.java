package Services;

public interface IEmailService {
    public void sendEmail(String recipientEmail, String subject, String messageBody);
}
