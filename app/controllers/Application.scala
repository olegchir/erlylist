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
    val programs = ProgramListHelper.loadProagams(channelName,serverURL,serverChannel,ProgramFileXML)
    Program.markRecordedFast(programs)
    Ok(views.html.index(programs))
  }

  def downloadProgramFileAction = Action {
    ProgramListHelper.downloadProgramFile(ProgramFileURL,ProgramFileZIP,ProgramFileXML)
    Redirect("/")
  }

}