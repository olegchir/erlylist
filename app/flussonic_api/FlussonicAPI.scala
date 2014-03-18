package flussonic_api

import play.api.libs.ws.{Response, WS}
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import com.ning.http.client._
import helpers.SystemSettings
import play.api.libs.json._
import play.api.libs.functional.syntax._
import scala.collection.mutable.ArrayBuffer

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

  /**
   * [{"stream":"euro","ranges":[{"from":1395129604,"duration":11365}]}]
   */
  def recordingStatus(serverURL:String, from: Long, to:Long, channel:String, waitFor:Duration = 10000 seconds):RecordingStatusResponse = {
    val connectURL = s"$serverURL/flussonic/api/recording_status"
    val request = WS.url(connectURL).withQueryString(("from",from.toString),("to",to.toString),("streams",channel)).withAuth(credentials.login,credentials.password, Realm.AuthScheme.BASIC)
    val promise: Future[Response] = request.get()
    val result = Await.result(promise,waitFor)
    val json:JsValue = Json.parse(result.body)(0)

    val stream = (json \ "stream").as[String]
    val ranges = new ArrayBuffer[RecordingStatusResponseRange]
    //https://groups.google.com/forum/#!topic/play-framework/y7V_UGalrFw
    (json \ "ranges").as[List[JsObject]].foreach(rangeJson => {
      ranges += RecordingStatusResponseRange((rangeJson \ "from").as[Long], (rangeJson \ "duration").as[Long])
    })
    RecordingStatusResponse(stream, ranges.toList)
  }
  case class RecordingStatusResponse (stream:String, ranges: List[RecordingStatusResponseRange])
  case class RecordingStatusResponseRange(from:Long, duration:Long)


}
