package controllers;

import play.*;
import play.i18n.Messages;
import play.mvc.*;

import java.util.*;

import models.*;

public class Application extends Controller {

    private static final String EMAIL_REGEX = "\\b[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b";

    private static final String SESSION_KEY_CODE = "code";

    public static void index() {
        String code = UUID.randomUUID().toString();
        session.put(SESSION_KEY_CODE, code);
        render(code);
    }

    public static void subscribe(String email, String code) {
        if (!session.contains(SESSION_KEY_CODE) ||
                !session.get(SESSION_KEY_CODE).equals(code)) {
            error("Abnormal request");
        }

        Map<String, String> response = new HashMap<String, String>();
        String errorMessage = null;
        if (!email.matches(EMAIL_REGEX)) {
            errorMessage = Messages.get("email.invalid");
        } else if (Subscriber.find("byEmail", email).fetch().size() > 0) {
            errorMessage = Messages.get("email.already_exists");
        } else {
            try {
                Subscriber.create(email.toLowerCase(), request.remoteAddress);
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