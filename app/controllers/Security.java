package controllers;

import play.Play;

/**
 * @author: Sergey Royz
 * @since: 26.03.2013
 */
public class Security extends Secure.Security {

    static boolean authenticate(String username, String password) {
        String admin = Play.configuration.getProperty("admin.user");
        String pass = Play.configuration.getProperty("admin.pass");

        if (admin.equals(username) &&
                pass.equals(password)) {
            return true;
        }
        return false;
    }

}
