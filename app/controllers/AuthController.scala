package controllers

import play.api.Logger
import play.api.Play.current
import model.repository.User
import play.api.cache.Cache
import play.api.data.Form
import play.api.data.Forms.{ text, tuple }
import play.api.i18n.Messages
import play.api.mvc._
import views.html

class AuthController extends Controller with Secured {

  /**
   * Describes the admin login form
   */
  val loginForm = Form(
    tuple(
      "email" -> text,
      "password" -> text) verifying ("Invalid email or password", result => result match {
        case (email, password) => check(email, password)
      }))

  /**
   * Checking of admin login credentials
   * @param username of admin
   * @param password of admin
   */
  def check(username: String, password: String): Boolean = {
    if (username == "admin" && password == "knol2013") {
      val user = User("admin", "1234")
      Cache.set(username, user, 60 * 60)
      true
    } else { false }
  }

  /**
   * Display the admin login form
   */
  def login: Action[AnyContent] = Action { implicit request =>
    Ok(html.admin.login(loginForm))
  }

  /**
   * Handle the admin login form submission
   */
  def authenticate: Action[AnyContent] = Action { implicit request =>
    loginForm.bindFromRequest.fold(
      formWithErrors => BadRequest(html.admin.login(formWithErrors)),
      user =>
        Redirect(routes.AdminController.mobiles("pending")).withSession(Security.username -> user._1))
  }

  /**
   * Handle admin logout
   */
  def logout: Action[AnyContent] = Action {
    Redirect(routes.AuthController.login).withNewSession.flashing(
      "success" -> "You are now logged out.")
  }
}

/**
 * Handle login security
 */
trait Secured {

  /**
   * Gets user from request
   */
  def username(request: RequestHeader): Option[String] = request.session.get(Security.username)

  /**
   * Handle unauthorized user
   */
<<<<<<< HEAD
  def onUnauthorized(request: RequestHeader): Result = {
=======
  def onUnauthorized(request: RequestHeader): play.api.mvc.Result = {
>>>>>>> d6c346eb6bc5960f6faf8baf3b89a64c6be95997
    Results.Redirect(routes.AuthController.login).withNewSession.flashing("success" -> Messages("messages.user.expired"))
  }

  def withAuth(f: => String => Request[play.api.mvc.AnyContent] => Result): play.api.mvc.EssentialAction = {
    Security.Authenticated(username, onUnauthorized) { username =>
      {
        val cachedUser: Option[User] = Cache.getAs[User](username)
        cachedUser match {
          case Some(user) => { Cache.set(username, user, 60 * 60); Action(request => f(username)(request)) }
          case None       => Action(request => onUnauthorized(request))
        }
      }
    }
  }
}

object AuthController extends AuthController
