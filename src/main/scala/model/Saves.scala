package model

import java.io.File
import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Paths}

import console.model.{Agent, Player, World}
import me.mtrupkin.console.screen.{RGBColor, ScreenChar}
import me.mtrupkin.geometry.Point
import play.api.libs.json._

/**
 * Created by mtrupkin on 11/30/2014.
 */

object Saves {
  implicit object WorldFormat extends Format[World] {
    implicit val PointFormat = Json.format[Point]
    implicit object RGBColorFormat extends Format[RGBColor] {
      def reads(json: JsValue): JsResult[RGBColor] = JsSuccess(RGBColor(json.as[String]))
      def writes(u: RGBColor): JsValue = JsString(u.toString)
    }

    implicit object ScreenCharFormat extends Format[ScreenChar] {
      def reads(json: JsValue): JsResult[ScreenChar] = JsSuccess(new ScreenChar(
        (json \ "c").as[String].charAt(0),
        (json \ "fg").as[RGBColor],
        (json \ "bg").as[RGBColor]))

      def writes(u: ScreenChar): JsValue = JsObject(List(
        "c" -> JsString(u.c.toString),
        "fg" -> Json.toJson(u.fg),
        "bg" -> Json.toJson(u.bg))
      )
    }

    implicit object AgentFormat extends Format[Agent] {
      def reads(json: JsValue): JsResult[Agent] = JsSuccess(new Agent(
        (json \ "name").as[String],
        (json \ "sc").as[ScreenChar],
        (json \ "position").as[Point],
        (json \ "hp").as[Int]))

      def writes(u: Agent): JsValue = JsObject(List(
        "name" -> JsString(u.name),
        "sc" -> Json.toJson(u.sc),
        "position" -> Json.toJson(u.position),
        "hp" -> JsNumber(u.hitPoints))
      )
    }

    implicit object PlayerFormat extends Format[Player] {
      def reads(json: JsValue): JsResult[Player] = JsSuccess(new Player(
        (json \ "name").as[String],
        (json \ "sc").as[ScreenChar],
        (json \ "position").as[Point],
        (json \ "hp").as[Int]))

      def writes(u: Player): JsValue = JsObject(List(
        "name" -> JsString(u.name),
        "sc" -> Json.toJson(u.sc),
        "position" -> Json.toJson(u.position),
        "hp" -> JsNumber(u.hitPoints))
      )
    }

    def reads(json: JsValue): JsResult[World] = JsSuccess(new World(
      (json \ "time").as[Long],
      (json \ "player").as[Player],
      (json \ "agents").asOpt[List[Agent]].getOrElse(List())))

    def writes(u: World): JsValue = JsObject(List(
      "time" -> JsNumber(u.time),
      "player" -> Json.toJson(u.player)(PlayerFormat),
      "agents" -> JsArray(u.agents.map(a => Json.toJson(a)))))
  }

  protected val saveDirectory = Paths.get(System.getProperty("user.dir"), "save")
  Files.createDirectories(saveDirectory)

  def names(): List[String] = {
    val names = for {
      saveFile <- saveDirectory.toFile.listFiles()
    } yield s"${saveFile.getName}"

    names.toList
  }

  def saveGame(name: String, world: World): Unit = {
    val js = Json.prettyPrint(Json.toJson(world))
    val f = new File(saveDirectory.toFile, name)

    val writer = Files.newBufferedWriter(f.toPath, StandardCharsets.UTF_8)
    writer.write(js)
    writer.close()

  }

  def loadGame(name: String): World = {
    val f = new File(saveDirectory.toFile, name)
    if (f.exists()) {
      val reader = Files.newBufferedReader(f.toPath, StandardCharsets.UTF_8)
      val builder = new StringBuilder()
      var aux = ""

      do {
        aux = reader.readLine()
        if (aux != null) builder.append(aux)
      } while (aux != null)


      val text = builder.toString()
      val js = Json.parse(text)
      Json.fromJson(js)(WorldFormat).getOrElse(throw new Exception)

    } else throw new Exception
  }

  def loadGame(i: Int): World = {
    loadGame(names()(i))
  }
}
