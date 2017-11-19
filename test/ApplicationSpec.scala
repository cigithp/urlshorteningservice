import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._
import play.api.mvc.SimpleResult
import play.api.test._
import play.api.test.Helpers._
import scala.concurrent.{Future}


/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
@RunWith(classOf[JUnitRunner])
class ApplicationSpec extends Specification {

  "Application" should {

    "send 404 on a bad request" in new WithApplication{
      route(FakeRequest(GET, "/boum")) must beNone
    }

    "create a shorten url" in new WithApplication {
      val data = ("longUrl", "http://www.google.com")
      val r = route(FakeRequest(POST, "/url").withHeaders("Content-Type" -> "application/x-www-form-urlencoded").withFormUrlEncodedBody(data)).get
      status(r) must equalTo(CREATED)
      contentAsString(r) must contain("1")
    }

    "create and retrieve a short url" in new WithApplication {
      val data = ("longUrl", "http://www.apple.com/iphone-7/")
      val data2 = ("inputShortURL", "http://localhost:9000/1")

      val r: Future[SimpleResult] = route(
        FakeRequest(POST, "/url").withHeaders("Content-Type" -> "application/x-www-form-urlencoded").withFormUrlEncodedBody(data)
      ).get
      status(r) must equalTo(CREATED)
      contentAsString(r) must contain("1")

      val r1: Future[SimpleResult] = route(
        FakeRequest(POST, "/shorturl").withHeaders("Content-Type" -> "application/x-www-form-urlencoded").withFormUrlEncodedBody(data,data2)
      ).get
      status(r1) must equalTo(SEE_OTHER)

    }

  }
}
