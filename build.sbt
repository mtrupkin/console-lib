import AssemblyKeys._

name := "console-lib"

version := "1.0"

scalaVersion := "2.10.3"

sbtVersion := "0.13.6"

libraryDependencies += "org.scala-lang" % "scala-swing" % "2.10.4"

packageArchetype.java_application

val zipit = TaskKey[File]("zip-it", "Create the zip file")

target in zipit := target.value / "output.zip"

zipit := {
  val executable = baseDirectory.value / "src" / "build" / "resources" / "console-app.exe"
  val java = new File(Option(System.getenv("JAVA_HOME")).getOrElse {
    throw new IllegalStateException("Missing JAVA_HOME")
  })
  val jre = java / "jre"
  val jreFiles = for {
    file <- jre.***.get if file.isFile
    name <- file.relativeTo(java)
  } yield file -> name.toString
  val output = (target in zipit).value
  val files = Seq(
    assembly.value -> "app/console-app.jar",
    executable -> "console-app.exe"
  ) ++ jreFiles
  IO.zip(files, output)
  output
}