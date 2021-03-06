package controllers;

import models.Page;
import models.Review;
import models.Subscriber;
import net.sf.uadetector.OperatingSystemFamily;
import net.sf.uadetector.ReadableUserAgent;
import net.sf.uadetector.UserAgentStringParser;
import net.sf.uadetector.service.UADetectorServiceFactory;
import org.apache.commons.lang.StringUtils;
import play.Logger;
import play.Play;
import play.i18n.Lang;
import play.i18n.Messages;
import play.mvc.Controller;
import service.Mails;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static net.sf.uadetector.OperatingSystemFamily.*;

public class Application extends Controller {

    private static final String EMAIL_REGEX = "\\b[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b";

    private static final String SESSION_KEY_CODE = "code";
    private static final String SESSION_KEY_LOCALE = "locale";

    private static final String UNSUBSCRIBE_FLASH_KEY_ERROR = "error";
    private static final String UNSUBSCRIBE_FLASH_KEY_SUCCESS = "success";


    public static void hotfix() {
        Lang.set("ru");
        render();
    }

    public static void index(String locale) {

        if (!"en".equals(locale)) {
            locale = "ru";
        }

        if (StringUtils.isEmpty(locale)) {
            locale = session.get(SESSION_KEY_LOCALE);
        } else {
            session.put(SESSION_KEY_LOCALE, locale);
        }

        locale = StringUtils.isEmpty(locale) ? "ru" : locale;

        Lang.set(locale);

        String code = UUID.randomUUID().toString();
        session.put(SESSION_KEY_CODE, code);

		String query = request.get().querystring;
		render(code, query);
    }

    public static void index_en() {
        session.put(SESSION_KEY_LOCALE, "en");
        Lang.set("en");
        String code = UUID.randomUUID().toString();
        session.put(SESSION_KEY_CODE, code);
        render(code);
    }

    public static void unsubscribe(String email) {
        if ("POST".equals(request.method)) {
            if (StringUtils.isEmpty(email)) {
                flash(UNSUBSCRIBE_FLASH_KEY_ERROR, Messages.get("unsubscribe.error.email_empty"));
            } else {
                Subscriber subscriber = Subscriber.findByEmail(email);
                if (subscriber != null) {
                    subscriber.unsubscribe();
                    flash(UNSUBSCRIBE_FLASH_KEY_SUCCESS, Messages.get("unsubscribe.success"));
                } else {
                    flash(UNSUBSCRIBE_FLASH_KEY_ERROR, Messages.get("unsubscribe.error.subscriber_not_found"));
                }
            }

        }
        render();
    }

    public static void subscribe(String name, String email, String code) {
        if (!session.contains(SESSION_KEY_CODE) ||
                !session.get(SESSION_KEY_CODE).equals(code)) {
            Logger.warn("Code in session: %s; code in request: %s", session.get(SESSION_KEY_CODE), code);
            forbidden("Session code is incorrect");
        }

        if (session.contains(SESSION_KEY_LOCALE)) {
            Lang.set(session.get(SESSION_KEY_LOCALE));
        }

        Map<String, String> response = new HashMap<String, String>();
        String errorMessage = null;
        if (StringUtils.isEmpty(name)) {
            errorMessage = Messages.get("name.empty");
        } else if (!email.matches(EMAIL_REGEX)) {
            errorMessage = Messages.get("email.invalid");
        } else if (Subscriber.isSubscribed(email)) {
            errorMessage = Messages.get("email.already_exists");
        } else {
            try {
                Subscriber subscriber = Subscriber.subscribe(name, email.toLowerCase(), request.remoteAddress);
                Mails.welcomeNew(subscriber.email);
                subscriber.updateNumericStatus(Subscriber.NUMERIC_STATUS_NEWLY_REGISTERED);
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

    public static void offer() {
        File file = new File("public/html/offer.html");
        Logger.info("Serving offer from path: " + file.getAbsolutePath());

        response.contentType = "text/html";
        renderBinary(file);
    }

    public static void install() {
        String userAgent = "" + request.headers.get("user-agent");
        UserAgentStringParser parser = UADetectorServiceFactory.getResourceModuleParser();
        ReadableUserAgent agent = parser.parse(userAgent);
        OperatingSystemFamily osFamily = agent.getOperatingSystem().getFamily();
        if (ANDROID.equals(osFamily)) {
            Logger.info("install(): android");
            redirect(Play.configuration.getProperty("instabank.playmarket.url"), true);
        } else if (IOS.equals(osFamily)) {
            Logger.info("install(): ios");
            redirect(Play.configuration.getProperty("instabank.appstore.url"), true);
        } else {
            Logger.info("install(): unknown: " + agent);
            index(null);
        }
    }

    public static void faq(String locale) {
        if (StringUtils.isEmpty(locale)) {
            locale = session.get(SESSION_KEY_LOCALE);
        } else {
            session.put(SESSION_KEY_LOCALE, locale);
        }

        locale = StringUtils.isEmpty(locale)? "ru": locale;

        Lang.set(locale);
//
//        List<FaqItem> items = FaqItem.list(locale);
//        render(items);
        Page page = Page.find("byKey", "faq").first();
        if (page != null) {
            render(page);
        } else {
            notFound("faq");
        }
    }

    public static void press(String locale) {
        if (StringUtils.isEmpty(locale)) {
            locale = session.get(SESSION_KEY_LOCALE);
        } else {
            session.put(SESSION_KEY_LOCALE, locale);
        }

        locale = StringUtils.isEmpty(locale) ? "ru" : locale;

        Lang.set(locale);

        List<Review> items = Review.list(locale);
        render(items);
    }

    public static void tariffs(String locale) {
        if (StringUtils.isEmpty(locale)) {
            locale = session.get(SESSION_KEY_LOCALE);
        } else {
            session.put(SESSION_KEY_LOCALE, locale);
        }

        locale = StringUtils.isEmpty(locale) ? "ru" : locale;

        Lang.set(locale);

        render();
    }

	public static void serveStatic(String filename) {
		File file = new File("public/html/" + filename + ".html");
		Logger.info("Serving offer from path: " + file.getAbsolutePath());

		response.contentType = "text/html";
		renderBinary(file);
	}

}