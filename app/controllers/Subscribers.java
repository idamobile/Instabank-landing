package controllers;

import play.mvc.With;

/**
 * @author: Sergey Royz
 * @since: 26.03.2013
 */
@With(Secure.class)
public class Subscribers extends CRUD {
}
