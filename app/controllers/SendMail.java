package controllers;

import org.apache.commons.lang.StringUtils;
import play.Logger;
import play.Play;
import play.mvc.Before;
import play.mvc.Controller;
import service.Mails;

/**
 * @author: Sergey Royz
 * @since: 29.05.2013
 */
public class SendMail extends Controller{

    private static final String CLASSIFIER_CARD_ORDERED_VALUE = "card_ordered";
    private static final String CLASSIFIER_USER_JOINED_VALUE = "user_joined";

    private static final String USERNAME_PROPERTY = "sendmail.user";
    private static final String PASSWORD_PASSWORD = "sendmail.pass";

    private static final String REALM = "Instabank";

    @Before
    static void authenticate() {
        String username = Play.configuration.getProperty(USERNAME_PROPERTY);
        String password = Play.configuration.getProperty(PASSWORD_PASSWORD);

        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            error("Sendmail is not configured");
        }

        if (!(username.equals(request.user) && password.equals(request.password))) {
            unauthorized(REALM);
        }

    }

    public static void send(String email, String classifier, String platform) {
        Logger.info(
                String.format("Sending notification to %s; classifier: %s; platform: %s", email, classifier, platform));
        if (CLASSIFIER_CARD_ORDERED_VALUE.equals(classifier)) {
            Mails.cardOrdered(email);
        } else if (CLASSIFIER_USER_JOINED_VALUE.equals(classifier)) {
            Mails.userJoined(email);
        }
        renderText("success");
    }

}
