package models

import org.joda.time._
import org.joda.time.tz._
import org.joda.time.format._
import java.util.Date
import helpers.TimeHelper
import flussonic_api.FlussonicAPI


/**
 * Created by olegchir on 18.03.14.
 */
case class Program (
start: String,
stop: String,
channel: String,
title: String,
category: String,
serverURL: String,
serverChannel: String
) {
  val startDateTime: DateTime = TimeHelper.programTimeToUTCDateTime(start)
  val stopDateTime: DateTime = TimeHelper.programTimeToUTCDateTime(stop)
  val startUnixtime = TimeHelper.fromJodaToUnix(startDateTime)
  val stopUnixtime = TimeHelper.fromJodaToUnix(stopDateTime)
  val lengthInSeconds = stopUnixtime-startUnixtime
  def archiveInMP4AsURL = FlussonicAPI.archiveInMP4AsURL(serverURL,serverChannel,startUnixtime,lengthInSeconds)
  def archiveInTSAsURL = FlussonicAPI.archiveInTSAsURL(serverURL,serverChannel,startUnixtime,lengthInSeconds)

  var recorded = false
}

object Program {

}