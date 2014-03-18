package controllers

import play.api._
import play.api.mvc._

import helpers._
import flussonic_api.FlussonicAPI

object Application extends Controller {
  val channelName = "EuroNews"
  val serverChannel = "euro"
  val serverURL = "http://demo.erlyvideo.ru"

  def index = Action {
    val programs = ProgramHelper.loadProagams(channelName,serverURL,serverChannel)

    programs.foreach(program => {
      val apiResponse = FlussonicAPI.recordingStatus(serverURL,program.startUnixtime,program.stopUnixtime,serverChannel)
      if (apiResponse.ranges.size>0) {program.recorded=true}
    })

    Ok(views.html.index(programs))
  }

  def downloadProgramFileAction = Action {
    ProgramHelper.downloadProgramFile
    Redirect("/")
  }

}