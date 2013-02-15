package jobs;

import models.Subscriber;
import play.Logger;
import play.jobs.Every;
import play.jobs.Job;
import service.Mails;

import java.util.List;

/**
 * @author: Sergey Royz
 * @since: 14.02.2013
 */
@Every("1h")
public class SendGreetingsJob extends Job {

    @Override
    public void doJob() throws Exception {
        List<Subscriber> subscribers = Subscriber.findByStatus(Subscriber.Status.NOT_CONFIRMED);
        Logger.info("%d users are pending invitation", subscribers.size());
        if (subscribers.isEmpty()) {
            return;
        }
        Subscriber subscriber = subscribers.get(0);
        Mails.welcomeExisting(subscriber.email);
        subscriber.updateStatus(Subscriber.Status.GREETING_SENT);
    }
}
