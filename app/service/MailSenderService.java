package service;

import models.Subscriber;
import play.Logger;
import play.db.jpa.Transactional;

/**
 * @author: Sergey Royz
 * @since: 14.02.2013
 */
public class MailSenderService {

    @Transactional
    public static void sendGreetingMessage(Subscriber subscriber) {
        try {
            Mails.welcome(subscriber.email);
            subscriber.status = Subscriber.Status.GREETING_SENT;
            subscriber.save();
            Logger.info("Greeting was sent to " + subscriber.email);
        }catch (Throwable t) {
            Logger.error(t, "Unable to send email");
        }
    }

}
