name := "console-lib"

organization := "org.trupkin"

version := "0.0.1"

scalaVersion := "2.10.4"

licenses += ("MIT", url("http://opensource.org/licenses/MIT"))

libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-swing" % "2.10.4",
  "me.lessis" %% "bintry" % "0.3.0"
)

addArtifact( Artifact("myproject", "dist", "zip"), launch4j)

publishTo := Some(Resolver.file("file",  new File( Path.userHome.absolutePath + "/file-repo"))( Patterns("[artifact](-[classifier]).[ext]") ))

publishMavenStyle := false
