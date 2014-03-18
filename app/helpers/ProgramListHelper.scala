package helpers

import sys.process._
import java.net.URL
import java.io.{FileInputStream, File}
import helpers.{FileStorageHelper => fsh}
import java.util.zip.GZIPInputStream
import java.nio.file._
import scala.xml._
import models.Program


object ProgramListHelper{
  var program = <document></document>

  def ensureProgramFileExists(programFileURL: String,programFileZIP: String,programFileXML: String) {
    val xmlFile = new File(programFileXML)
    if (!xmlFile.exists()) {
      downloadProgramFile(programFileURL,programFileZIP,programFileXML)
    }
  }

  def downloadProgramFile(programFileURL: String,programFileZIP: String,programFileXML: String) = {
    val xmlFile = new File(programFileXML)
    val zipFile = new File(programFileZIP)

    zipFile.delete
    xmlFile.delete

    new URL(programFileURL) #> new File(programFileZIP) !!

    val zipInputStream = new GZIPInputStream(new FileInputStream(zipFile))
    try {Files.copy(zipInputStream,Paths.get(programFileXML))}
    finally {zipInputStream.close()}

    program = scala.xml.XML.loadFile(programFileXML)
  }

  def attributeEquals(name: String, value: String)(node: Node) =
  {
    node.attribute(name).filter(_.toString()==value).isDefined
  }

  def channelEquals(channelName: String)(node: Node) = {
    ((node \ "display-name").text.toString())==channelName
  }

  def loadProagams(channelName: String, serverURL: String, serverChannel: String, programFileXML: String) = {
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
        category = (n \ "category").text.toString(),
        serverURL = serverURL,
        serverChannel = serverChannel
      )))

    programs
  }
}
