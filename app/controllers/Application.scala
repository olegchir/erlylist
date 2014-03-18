package controllers

import play.api._
import play.api.mvc._

import helpers._

object Application extends Controller {
  val channelName = "EuroNews"

  def index = Action {
    val programs = ProgramHelper.loadProagams(channelName)
    Ok(views.html.index(programs))
  }

  def downloadProgramFileAction = Action {
    ProgramHelper.downloadProgramFile
    Redirect("/")
  }

}