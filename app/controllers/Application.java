package controllers;

import models.Subscriber;
import play.Logger;
import play.i18n.Messages;
import play.mvc.Controller;
import service.Mails;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Application extends Controller {

    private static final String EMAIL_REGEX = "\\b[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b";

    private static final String SESSION_KEY_CODE = "code";

    public static void indexNew() {
        String code = UUID.randomUUID().toString();
        session.put(SESSION_KEY_CODE, code);
        render(code);
    }

    public static void index() {
        String code = UUID.randomUUID().toString();
        session.put(SESSION_KEY_CODE, code);
        render(code);
    }

    public static void unsubscribe(String email) {
        Subscriber s = Subscriber.find("byEmail", email).first();
        if (s != null) {
            s.status = Subscriber.Status.UNSUBSCRIBED;
            s.save();
            Logger.info("User %s was unsubscribed", email);
        }

        index();
    }

    public static void subscribe(String email, String code) {
        if (!session.contains(SESSION_KEY_CODE) ||
                !session.get(SESSION_KEY_CODE).equals(code)) {
            Logger.warn("Code in session: %s; code in request: %s", session.get(SESSION_KEY_CODE), code);
            forbidden("Session code is incorrect");
        }

        Map<String, String> response = new HashMap<String, String>();
        String errorMessage = null;
        if (!email.matches(EMAIL_REGEX)) {
            errorMessage = Messages.get("email.invalid");
        } else if (Subscriber.find("byEmail", email).fetch().size() > 0) {
            errorMessage = Messages.get("email.already_exists");
        } else {
            try {
                Subscriber subscriber = Subscriber.create(email.toLowerCase(), request.remoteAddress);
                Mails.welcomeNew(subscriber.email);
                subscriber.updateStatus(Subscriber.Status.GREETING_SENT);
            } catch (Exception ex) {
                errorMessage = Messages.get("email.persisting_error") + ex.getMessage();
                Logger.error("Unable to persist email", ex);
            }
        }
        if (errorMessage == null) {
            response.put("result", "true");
            response.put("msg", Messages.get("email.saved"));
        } else {
            response.put("result", "false");
            response.put("msg", errorMessage);
        }
        renderJSON(response);
    }

}