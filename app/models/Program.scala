package models

import org.joda.time._
import org.joda.time.tz._
import org.joda.time.format._
import java.util.Date
import helpers.TimeHelper
import flussonic_api.FlussonicAPI

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
  def markRecordedFast(sourcePrograms:List[Program]) = {
    val programs = sourcePrograms.sortBy(_.startUnixtime)
    if (programs.size>0) {
      val headElem = programs.head
      val serverURL = headElem.serverURL
      val serverChannel = headElem.serverChannel
      val minUnixtime = programs.head.startUnixtime
      val maxUnixtime = programs.last.stopUnixtime
      val recordingStatus = FlussonicAPI.recordingStatus(serverURL,minUnixtime,maxUnixtime,serverChannel)
      val programsRecorded = programs.filter(program => {
        recordingStatus.ranges.filter(rng => {
          val startTime = rng.from
          val endTime = rng.from + rng.duration
          (startTime <= program.startUnixtime && endTime >= program.startUnixtime) ||
            (startTime <= program.stopUnixtime && endTime >= program.stopUnixtime) ||
            (startTime <= program.startUnixtime && endTime >= program.stopUnixtime) ||
            (startTime >= program.startUnixtime && endTime <= program.stopUnixtime)
        }).size>0
      })
      programsRecorded.foreach(_.recorded=true)
    }
  }

  def markRecordedWithMultipleAPICalls(sourcePrograms:List[Program]) {
    if (sourcePrograms.size>0) {
      val headElem = sourcePrograms.head
      val serverURL = headElem.serverURL
      val serverChannel = headElem.serverChannel

      sourcePrograms.foreach(program => {
        val apiResponse = FlussonicAPI.recordingStatus(serverURL,program.startUnixtime,program.stopUnixtime,serverChannel)
        if (apiResponse.ranges.size>0) {program.recorded=true}
      })
    }
  }
}