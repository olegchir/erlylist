package helpers

import org.joda.time._
import org.joda.time.tz._
import org.joda.time.format._
import java.util.{Locale, Date}


object TimeHelper {
  def programTimeToDateTime(tm: String) = DateTimeFormat.forPattern("yyyyMMddHHmmss Z").withOffsetParsed().parseDateTime(tm)
  def programTimeToUTCDateTime(tm: String) = programTimeToDateTime(tm).toDateTime(DateTimeZone.UTC)

  def fromUnixToJoda(tm: Long) = new DateTime(tm * 1000L)
  def fromUnixToJoda(tm: String) = new DateTime(tm.toLong * 1000L)

  def fromJodaToUnix(tm:DateTime, tz:DateTimeZone = DateTimeZone.UTC) = tm.toDateTime(tz).getMillis()/1000

  def jodaForHuman(tm:DateTime, pattern:String = "E, d MMM yyyy HH:mm:ss", locale:Locale=Locale.US) = tm.toString(DateTimeFormat.forPattern(pattern).withLocale(locale))
}
