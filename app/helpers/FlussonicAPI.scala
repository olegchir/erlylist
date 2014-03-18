package helpers

import play.api.libs.ws.{Response, WS}
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import com.ning.http.client._

class ApiCredentials(var login: String, var password: String)

object ApiCredentinals {
  def get() = {
    val login:String = SystemSettings.getIniFile.get("api","login").toString
    val password:String = SystemSettings.getIniFile.get("api","password").toString
    new ApiCredentials(login,password)
  }
}

/**
 * Created by olegchir on 18.03.14.
 */
object FlussonicAPI {
  def credentials = ApiCredentinals.get()

  def dvrStatus(serverURL:String, year:String, month:String, day:String, channel:String, waitFor:Duration = 10000 seconds):Unit = {
    val connectURL = s"$serverURL/flussonic/api/dvr_status/$year/$month/$day/$channel"
    val request = WS.url(connectURL)
    val promise: Future[Response] = request.get()
    val result = Await.result(promise,waitFor)
    println(result.body)
  }

  def recordingStatus(serverURL:String, from: String, to:String, channel:String, waitFor:Duration = 10000 seconds):Unit = {
    val connectURL = s"$serverURL/flussonic/api/recording_status"
    val request = WS.url(connectURL).withQueryString(("from",from),("to",to),("streams",channel)).withAuth(credentials.login,credentials.password, Realm.AuthScheme.BASIC)
    val promise: Future[Response] = request.get()
    val result = Await.result(promise,waitFor)
    println(result.body)
  }
  
}
