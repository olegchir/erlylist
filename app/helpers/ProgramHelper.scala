package helpers

import sys.process._
import java.net.URL
import java.io.{FileInputStream, File}
import helpers.{FileStorageHelper => fsh}
import java.util.zip.GZIPInputStream
import java.nio.file._
import scala.xml._
import models.Program

/**
 * Created by olegchir on 18.03.14.
 */
object ProgramHelper {
  val ProgramFileURL="http://www.teleguide.info/download/new3/xmltv.xml.gz"
  val ProgramFileZIP=fsh.appendElement(fsh.rootPath,"/xmltv.xml.gz")
  val ProgramFileXML=fsh.appendElement(fsh.rootPath,"/xmltv.xml")

  def downloadProgramFile = {
    val xmlFile = new File(ProgramFileXML)
    val zipFile = new File(ProgramFileZIP)

    zipFile.delete
    xmlFile.delete

    new URL(ProgramFileURL) #> new File(ProgramFileZIP) !!

    val zipInputStream = new GZIPInputStream(new FileInputStream(zipFile))
    try {Files.copy(zipInputStream,Paths.get(ProgramFileXML))}
    finally {zipInputStream.close()}
  }

  def attributeEquals(name: String, value: String)(node: Node) =
  {
    node.attribute(name).filter(_.toString()==value).isDefined
  }

  def channelEquals(channelName: String)(node: Node) = {
    ((node \ "display-name").text.toString())==channelName
  }

  def loadProagams(channelName: String) = {
    val program = scala.xml.XML.loadFile(ProgramFileXML)

    val channelId = (program \ "channel" filter channelEquals(channelName)) collectFirst {case n => n.attribute("id") match {case Some(x) => x.toString}} match {
      case Some(chennelNum:String) => chennelNum
      case _ => "-1"
    }

    //val program = <document> <programme start="20140319142500 +0400" stop="20140319150000 +0400" channel="208"> <title lang="ru">Понять. Простить</title> <category lang="ru">Познавательные</category> </programme> </document>
    val programs: List[Program] = List[Program]() ++ (program \ "programme" filter attributeEquals("channel", channelId) collect PartialFunction.apply(
      n => Program(
        start = (n \ "@start").toString(),
        stop = (n \ "@stop").toString(),
        channel = (n \ "@channel").toString(),
        title = (n \ "title").text.toString(),
        category = (n \ "category").text.toString())))

    programs
  }
}
