package controllers;

import play.mvc.With;

/**
 * @author: Sergey Royz
 * @since: 02.12.2013
 */

@With(Secure.class)
public class Pages extends CRUD {
}
