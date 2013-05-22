package controllers;

import play.mvc.With;

/**
 * @author: Sergey Royz
 * @since: 22.05.2013
 */

@With(Secure.class)
public class FaqItems extends CRUD {
}
