package controllers

import play.api._
import play.api.mvc._

import helpers._
import flussonic_api.FlussonicAPI
import models.Program

object Application extends Controller {
  val channelName = "EuroNews"
  val ProgramFileURL="http://www.teleguide.info/download/new3/xmltv.xml.gz"
  val ProgramFileZIP=FileStorageHelper.appendElement(FileStorageHelper.rootPath,"/xmltv.xml.gz")
  val ProgramFileXML=FileStorageHelper.appendElement(FileStorageHelper.rootPath,"/xmltv.xml")
  val serverChannel = "euro"
  val serverURL = "http://demo.erlyvideo.ru"

  def index = Action {
    ProgramListHelper.ensureProgramFileExists(ProgramFileURL,ProgramFileZIP,ProgramFileXML)
    val programs = ProgramListHelper.loadProagams(channelName,serverURL,serverChannel,ProgramFileXML)
    Program.markRecordedFast(programs)
    Ok(views.html.index(programs,channelName))
  }

  def downloadProgramFileAction = Action {
    ProgramListHelper.downloadProgramFile(ProgramFileURL,ProgramFileZIP,ProgramFileXML)
    Redirect("/")
  }

  def HDSPlayer(from:Long,duration:Long) = Action {
    val videoURL = FlussonicAPI.archiveInF4MAsURL(serverURL,serverChannel,from,duration)
    Ok(views.html.hdsplayer(videoURL,serverURL,serverChannel))
  }

  def HDSRewindPlayer(from:Long) = Action {
    val videoURL = FlussonicAPI.archiveInF4MTimeshiftWithRewindAsURL(serverURL,serverChannel,from)
    Ok(views.html.hdsplayer(videoURL,serverURL,serverChannel))
  }

  def HLSPlayer(from:Long,duration:Long,mono:Boolean) = Action {
    val videoURL = FlussonicAPI.archiveInM3U8AsURL(serverURL,serverChannel,from,duration,mono)
    Ok(views.html.hlsplayer(videoURL,serverURL,serverChannel))
  }

  def HLSRewindPlayer(from:Long,mono:Boolean) = Action {
    val videoURL = FlussonicAPI.archiveInM3U8TimeshiftWithRewindWithoutRedirectAsURL(serverURL,serverChannel,from,mono)
    Ok(views.html.hlsplayer(videoURL,serverURL,serverChannel))
  }

}