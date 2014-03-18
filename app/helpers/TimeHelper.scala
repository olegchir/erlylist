package helpers

import org.joda.time._
import org.joda.time.tz._
import org.joda.time.format._
import java.util.Date


object TimeHelper {
  def programTimeToDateTime(tm: String) = DateTimeFormat.forPattern("yyyyMMddHHmmss Z").withOffsetParsed().parseDateTime(tm)
  def programTimeToUTCDateTime(tm: String) = programTimeToDateTime(tm).toDateTime(DateTimeZone.UTC)

  def fromUnixToJoda(tm: Long) = new DateTime(tm * 1000L)
  def fromUnixToJoda(tm: String) = new DateTime(tm.toLong * 1000L)

  def fromJodaToUnix(tm:DateTime, tz:DateTimeZone = DateTimeZone.UTC) = tm.toDateTime(tz).getMillis()/1000
}
