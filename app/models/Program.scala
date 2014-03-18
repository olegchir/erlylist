package models

import org.joda.time._
import org.joda.time.tz._
import org.joda.time.format._
import java.util.Date
import helpers.TimeHelper


/**
 * Created by olegchir on 18.03.14.
 */
case class Program (
start: String,
stop: String,
channel: String,
title: String,
category: String
) {
  val startDateTime: DateTime = TimeHelper.programTimeToUTCDateTime(start)
  val stopDateTime: DateTime = TimeHelper.programTimeToUTCDateTime(stop)
  val startUnixtime = TimeHelper.fromJodaToUnix(startDateTime)
  val stopUnixtime = TimeHelper.fromJodaToUnix(stopDateTime)

  val recorded = false
}

object Program {

}