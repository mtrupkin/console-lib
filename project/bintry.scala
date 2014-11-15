import sbt._
import Keys._
import bintry._, dispatch._, dispatch.Defaults._, org.json4s._

object BintryBuild extends Build {
	val user = "mtrupkin"
	val bty = Client(user, "XXX")
	val repo = bty.repo(user, "test")
	val pack = repo.get("console-lib")
    //val ver1 = pack.createVersion("0.0.2")(as.json4s.Json)
    val ver = pack.version("0.0.2")

	val publishBintray = TaskKey[Unit]("publish-bintray", "Publish to Bintray")

	override lazy val settings = super.settings ++
	    Seq(
	      publishBintray := runPublishBintray.value
	    )

	private def runPublishBintray: Def.Initialize[Task[Unit]] = Def.task {
		println("runPublishBintray")
		val f = new File(Path.userHome.absolutePath + "/file-repo", "console-lib-0.0.1.zip")
		println(f.toString)
		ver.upload("console-lib.zip", f).publish(true)(as.json4s.Json)
		println("completed")
	}
}
