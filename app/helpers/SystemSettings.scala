package helpers

import java.io.File
import java.util.prefs.Preferences
import org.ini4j.Ini
import helpers.{FileStorageHelper => fsh}

object SystemSettings {
  def getIniFile:Ini = {
    val result = new Ini(new File(fsh.appendElement(fsh.rootPath,"settings.ini")))
    result.load()
    result
  }
}
