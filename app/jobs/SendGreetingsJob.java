package jobs;

import models.Subscriber;
import play.Logger;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import service.Mails;

import java.util.List;

/**
 * @author: Sergey Royz
 * @since: 14.02.2013
 */
@OnApplicationStart
public class SendGreetingsJob extends Job {

    @Override
    public void doJob() throws Exception {
        List<Subscriber> subscribers = Subscriber.findByStatus(Subscriber.Status.NOT_CONFIRMED);
        Logger.info("Sending greetings to %d users", subscribers.size());
        for (Subscriber subscriber: subscribers) {
            Mails.welcomeExisting(subscriber.email);
            subscriber.updateStatus(Subscriber.Status.GREETING_SENT);
        }
    }
}
