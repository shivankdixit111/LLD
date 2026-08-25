package Low_Level_Design.Behavioural_Design_Patterns.Observer;

import java.util.ArrayList;
import java.util.List;

interface NotificationChannel {
    void notifySubscriber(String video);
}

class EmailNotification implements NotificationChannel {
    public String email;
    EmailNotification(String email) {
        this.email = email;
    }
    @Override
    public void notifySubscriber(String video) {
        System.out.println("Email Notification sent to " + email + " for the video : " + video + "\n");
    }
}
class PushNotification implements NotificationChannel {
    public String deviceToken;
    PushNotification(String deviceToken) {
        this.deviceToken = deviceToken;
    }
    @Override
    public void notifySubscriber(String video) {
        System.out.println("Push Notification sent to " + deviceToken + " for the video : " + video + "\n");
    }
}

// 1. The Observer Interface 📝
interface Subscriber {
    void update(String video);
}

// 2. Concrete Observer Class 👀
class YouTubeSubscriber implements Subscriber {
    String name;
    List<NotificationChannel> channels = new ArrayList<>();

    YouTubeSubscriber(String name) {
        this.name = name;
    }
    public void addNotificationChannel(NotificationChannel channel) {
        channels.add(channel);
    }

    @Override
    public void update(String video) {
        // System.out.println(name + " watching the video : " + video);
        for(NotificationChannel channel: channels) {
            channel.notifySubscriber(video);
        }
    }
}

// 3. The Subject Interface 🗣️
interface YouTubeChannel {
    void addSubscriber(Subscriber subscriber);
    void removeSubscriber(Subscriber subscriber);
    void notifySubscribers(); 
}

// 4. Concrete Subject Class 🖥️
class YoutubeChannelImp implements YouTubeChannel {
    private List<Subscriber> subscribers = new ArrayList<>();
    private String video;
    
    @Override
    public void addSubscriber(Subscriber subscriber) {
        subscribers.add(subscriber);
    }
    @Override
    public void removeSubscriber(Subscriber subscriber) {
        subscribers.remove(subscriber);
    }
    @Override
    public void notifySubscribers() {
        for(Subscriber subscriber : subscribers) {
            subscriber.update(video);
        }
    } 

    public void uploadNewVideo(String video) {
        this.video = video;
        notifySubscribers();
    }
}


// 5. Driver Code: Putting It All Together 🎬
public class Main {
    public static void main(String[] args) {
        YoutubeChannelImp channel = new YoutubeChannelImp();
        YouTubeSubscriber alice = new YouTubeSubscriber("alice");
        alice.addNotificationChannel(new EmailNotification("alice@gmail.com"));
        alice.addNotificationChannel(new PushNotification("device123"));

        YouTubeSubscriber bob = new YouTubeSubscriber("bob");
        bob.addNotificationChannel(new PushNotification("device987"));

        channel.addSubscriber(alice);
        channel.addSubscriber(bob);
        
        channel.uploadNewVideo("LLD basic in 1 hr");

        channel.removeSubscriber(alice);

        channel.uploadNewVideo("HLD basic in 1 hr");
    }
}


/**
 * ============================
 * FINAL VERDICT (LLD DESIGN)
 * ============================
 *
 * This implementation correctly demonstrates:
 *
 * ✔ Observer Design Pattern
 * ✔ Strategy Design Pattern (for notifications)
 * ✔ Open/Closed Principle (OCP)
 * ✔ Loose Coupling between components
 *
 * ============================
 * OBSERVER PATTERN STRUCTURE
 * ============================
 *
 * Subject (Publisher)
 *      -> YouTubeChannel (interface)
 *      -> YouTubeChannelImpl (concrete subject)
 *
 * Observer (Subscriber)
 *      -> Subscriber (interface)
 *      -> YouTubeSubscriber (concrete observer)
 *
 * Flow:
 *      YouTubeChannelImpl
 *              |
 *              | notifySubscribers()
 *              v
 *      Subscriber.update(video)
 *              |
 *              v
 *      YouTubeSubscriber
 *
 * ============================
 * STRATEGY PATTERN STRUCTURE
 * ============================
 *
 * Notification Strategy (pluggable behavior)
 *      -> NotificationChannel (interface)
 *      -> EmailNotification (concrete strategy)
 *      -> PushNotification (concrete strategy)
 *
 * Flow:
 *      YouTubeSubscriber
 *              |
 *              | delegates notification
 *              v
 *      NotificationChannel
 *          /              \
 * EmailNotification   PushNotification
 *
 * ============================
 * KEY IDEA
 * ============================
 *
 * - YouTubeChannel does NOT know about subscribers' internal logic
 * - Subscriber does NOT know how notifications are sent internally
 * - Notification logic is completely extensible without modifying existing code
 *
 * This ensures:
 *      ✔ High scalability
 *      ✔ Easy extension (add SMS/WhatsApp without changes)
 *      ✔ Clean separation of concerns
 */