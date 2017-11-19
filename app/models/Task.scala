package models

import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current

case class Task(id: Long, longUrl: String)

object Task {

  // parser to read task, after we get result set from DB
  val url = {
    get[Long]("id") ~
      get[String]("longurl") map {
      case id~longUrl => Task(id, longUrl)
    }
  }

  def all(): List[Task] = DB.withConnection { implicit c =>
    // Anorm sql method to query
    val x = SQL("select * from url").as(url *)
    x
  }

  def read(inputURL: String): Option[Task] = {
    DB.withConnection { implicit c =>
      // Anorm sql method to query
      SQL(s"select * from url where longurl = '$inputURL'").as(url *).headOption
    }
  }

  def getLongUrl(sUrl: String): Option[Task] = {
    DB.withConnection { implicit c =>
      // Anorm sql method to query
      SQL(s"select * from url where id = '$sUrl'").as(url *).headOption
    }
  }

  def create(inputURL: String): String = {
    DB.withConnection { implicit c =>
      read(inputURL) match {
        case Some(task) => task.id.toString
        case _ =>
          SQL("insert into url (longurl) values ({url})").on(
          'url -> inputURL
        ).executeUpdate()
          read(inputURL).get.id.toString
      }
    }
  }

  def delete(id: Long) {
    DB.withConnection { implicit c =>
      SQL("delete from url where id = {id}").on(
        'id -> id
      ).executeUpdate()
    }
  }

}
