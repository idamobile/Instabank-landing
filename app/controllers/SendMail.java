package controllers;

import org.apache.commons.lang.StringUtils;
import play.Logger;
import play.Play;
import play.mvc.Before;
import play.mvc.Controller;

/**
 * @author: Sergey Royz
 * @since: 29.05.2013
 */
public class SendMail extends Controller{

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

    public static void send(String email, String classifier) {
        Logger.info("Sending email to: %s; type: %s", email, classifier);
        renderText("Hello world: ");
    }

}
