name := "erlylist"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache
)

//https://github.com/scala/async
libraryDependencies += "org.scala-lang.modules" %% "scala-async" % "0.9.0-M6"

libraryDependencies += "org.ini4j" % "ini4j" % "0.5.2"

play.Project.playScalaSettings
