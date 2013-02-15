package service;

import play.Logger;
import play.Play;
import play.i18n.Messages;
import play.mvc.Mailer;

/**
 * @author: Sergey Royz
 * @since: 14.02.2013
 */
public class Mails extends Mailer {

    public static void welcomeNew(String recipient) {
        addRecipient(recipient);
        setFrom(Play.configuration.getProperty("mail.smtp.sender.email"));
        setSubject(Messages.get("email.subject.new"));
        try {
            send();
            Logger.info("New user notified: " + recipient);
        } catch (Throwable t) {
            Logger.error(t, "Unable to send email to recipient: " + recipient);
        }
    }

    public static void welcomeExisting(String recipient) {
        addRecipient(recipient);
        setFrom(Play.configuration.getProperty("mail.smtp.sender.email"));
        setSubject(Messages.get("email.subject.existing"));
        try {
            send();
            Logger.info("Existing user notified: " + recipient);
        } catch (Throwable t) {
            Logger.error(t, "Unable to send email to recipient: " + recipient);
        }
    }

}
