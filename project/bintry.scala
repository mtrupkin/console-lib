import bintry._
import dispatch.Defaults._
import dispatch._
import sbt._

object BintryBuild extends Build {
	val user = "mtrupkin"
	val bty = Client(user, "8189233b0eedd88871565ae0acbf641330e5394f")
	val repo = bty.repo(user, "test")
	val pack = repo.get("console-lib")
    //val ver1 = pack.createVersion("0.0.2")(as.json4s.Json)
    val ver = pack.version("0.0.2")

	val publishBintray2 = TaskKey[Unit]("publish-bintray", "Publish to Bintray")

	override lazy val settings = super.settings ++
	    Seq(
	      publishBintray2 := runPublishBintray.value
	    )

	private def runPublishBintray: Def.Initialize[Task[Unit]] = Def.task {
		println("runPublishBintray")
		//val f = buildSfx.value
		//println(f.toString)
		//ver.upload(f.name, f).publish(true)(as.json4s.Json)
		println("completed")
	}
}
