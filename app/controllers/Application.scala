package controllers

import models.{Task}
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._


object Application extends Controller {
  val domain = "http://localhost:9000/"

  val taskForm = Form(
    "longUrl" -> nonEmptyText
  )

  val taskForm2 = Form(
    "inputShortURL" -> nonEmptyText
  )

  def index = Action {
    Ok(views.html.index("", taskForm, taskForm2))
  }

  def generateURL = Action { implicit request =>
    taskForm.bindFromRequest.fold(
      errors => BadRequest(views.html.index("Error", errors, errors)),
      url => {
        Created(views.html.index(domain + Task.create(url), taskForm, taskForm2))
      }
    )
  }

  def browseShortURL = Action { implicit request =>
    taskForm2.bindFromRequest.fold(
      errors => BadRequest(views.html.index("Error", errors, errors)),
      url => {
        Task.getLongUrl(url.substring(url.lastIndexOf('/') + 1)) match {
          case Some(task) =>
            Redirect(task.longUrl)
          case _ =>
            BadRequest("URL NOT FOUND !!")
        }
      }
    )
  }

}