package helpers

import java.io.File

object FileStorageHelper {
  val fsep = File.separator
  val appname = "erlylist"
  new File(rootPath).mkdirs()

  def rootPath:String = {
    if (isWindows) s"X:\\store\\$appname" else s"/my/store/$appname"
  }

  def appendElement(source: String, newElement: String):String = {
    source + fsep + newElement
  }

  def imagePath:String = {
    rootPath+fsep+"images"
  }

  def filePath:String = {
    rootPath+fsep+"files"
  }

  def isWindows:Boolean = {
    val OSName = System.getProperty("os.name")
    OSName.startsWith("Windows")
  }
}
