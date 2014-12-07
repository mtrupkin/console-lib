import dispatch.as
import sbt.Keys._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Await
import scala.concurrent.duration.Duration

name := "console-lib"

organization := "org.trupkin"

scalaVersion := "2.11.4"

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-json" % "2.4.0-M2",
  "org.scala-lang" % "scala-swing" % "2.11.0-M7",
  "commons-io" % "commons-io" % "2.4"
)

licenses += ("MIT", url("http://opensource.org/licenses/MIT"))

releaseSettings

jreHome := (target in buildLauncher).value / "jre"

lazy val extractJRE = TaskKey[File]("extract-jre", "Extract embedded JRE")

extractJRE := {
  val launcherHome = (target in buildLauncher).value
  launcherHome.mkdirs()
  val jreZip = sourceDirectory.value / "build" / "windows" / "jre.zip"
  IO.unzip(jreZip, launcherHome)
  jreHome.value
}

buildLauncher <<= buildLauncher.dependsOn(extractJRE)

publishTo := Some(Resolver.file("file",  new File(Path.userHome.absolutePath+"/.m2/repository"))) // make sbt-release happy

publish := {
  val user = "mtrupkin"
  val owner = user
  val apiKey = "8189233b0eedd88871565ae0acbf641330e5394f"
  val bintrayRepository = "test"
  val bintrayPackage = "console-lib"
  val uploadFile = buildSfx.value
  val bty = bintry.Client(user, apiKey)
  val repo = bty.repo(owner, bintrayRepository)
  val pack = repo.get(bintrayPackage)
  println(s"create version ${version.value}")
  val verFuture = pack.createVersion(version.value)(as.json4s.Json)
  Await.result(verFuture, Duration.Inf)
  println("create version completed")
  val ver = pack.version(version.value)
  println(s"upload ${uploadFile.toString}")
  val uploadFuture = ver.upload(uploadFile.name, uploadFile).publish(true)(as.json4s.Json)
  Await.result(uploadFuture, Duration.Inf)
  println("upload completed")
}
