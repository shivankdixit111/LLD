// gemini ref : https://share.gemini.google/RD1TAFyquHAX

package Low_Level_Design.LLD_Scale_Mock.Solid_Principles_and_OOPS_Design.Notification_Service_With_DIP;

import java.util.List; 

// 1. Low-Level Channel Abstraction
interface MessageSender {
    void send(String recipient, String message);
}

// 2. Concrete Channel Implementations
class EmailSender implements MessageSender {
    @Override
    public void send(String recipient, String message) {
        System.out.println("[EMAIL] To: " + recipient + " | Message: " + message);
    }
}

class SmsSender implements MessageSender {
    @Override
    public void send(String recipient, String message) {
        System.out.println("[SMS] To: " + recipient + " | Message: " + message);
    }
}

class PushSender implements MessageSender {
    @Override
    public void send(String recipient, String message) {
        System.out.println("[PUSH] To: " + recipient + " | Message: " + message);
    }
}

// 3. High-Level Orchestrator (Supports Multi-Channel via Constructor Injection)
class NotificationService {
    private final List<MessageSender> senders;

    // Constructor Injection allowing 1 to N channels
    public NotificationService(List<MessageSender> senders) {
        if (senders == null || senders.isEmpty()) {
            throw new IllegalArgumentException("At least one MessageSender channel must be provided.");
        }
        this.senders = List.copyOf(senders);
    }

    public void notifyUser(String recipient, String message) {
        for (MessageSender sender : senders) {
            sender.send(recipient, message);
        }
    }
}

// 4. Client / Application Entry Point
public class Main {
    public static void main(String[] args) {
        // Wiring dependencies externally (IoC Container simulation)
        MessageSender emailChannel = new EmailSender();
        MessageSender pushChannel = new PushSender();

        // Dispatching via multi-channel setup
        NotificationService multiChannelService = new NotificationService(
            List.of(emailChannel, pushChannel)
        );
        multiChannelService.notifyUser("user_123", "Your order has shipped!");

        // Single channel swap without editing NotificationService
        NotificationService smsOnlyService = new NotificationService(
            List.of(new SmsSender())
        );
        smsOnlyService.notifyUser("+1234567890", "Your OTP is 4821");
    }
}