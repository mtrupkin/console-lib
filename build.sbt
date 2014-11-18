import bintry.Client
import dispatch.as

name := "console-lib"

organization := "org.trupkin"

version := "0.0.1"

scalaVersion := "2.10.4"

licenses += ("MIT", url("http://opensource.org/licenses/MIT"))

libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-swing" % "2.10.4"
)

publishTo := Some(Resolver.file("file",  new File( Path.userHome.absolutePath + "/file-repo"))( Patterns("[artifact](-[classifier]).[ext]") ))

publishMavenStyle := false

lazy val publishBintray = taskKey[Unit]("Publish to Bintray")

publishBintray := {
  val user = "mtrupkin"
  val bty = Client(user, "8189233b0eedd88871565ae0acbf641330e5394f")
  val repo = bty.repo(user, "test")
  val pack = repo.get("console-lib")
  val ver = pack.version("0.0.2")
  println("runPublishBintray")
  val f = buildSfx.value
  println(f.toString)
  ver.upload(f.name, f).publish(true)(as.json4s.Json)
  println("completed")
}
