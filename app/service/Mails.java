package service;

import play.Play;
import play.i18n.Messages;
import play.mvc.Mailer;

/**
 * @author: Sergey Royz
 * @since: 14.02.2013
 */
public class Mails extends Mailer {

    public static void welcome(String recipient) {
        addRecipient(recipient);
        setFrom(Play.configuration.getProperty("mail.smtp.sender.email"));
        setSubject(Messages.get("email.subject"));
        send();
    }
}
