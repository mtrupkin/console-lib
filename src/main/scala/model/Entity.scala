package console.model

import me.mtrupkin.console.screen.ScreenChar
import me.mtrupkin.geometry.Point

// Created: 4/8/2014

trait Entity {
  val name: String
  def sc: ScreenChar
  def position: Point
  def hitPoints: Int
}

class Agent(
  val name: String,
  val sc: ScreenChar,
  var position: Point,
  var hitPoints: Int) extends Entity {

}

class Player(name: String, sc: ScreenChar, p: Point, hp: Int)
  extends Agent (name, sc, p, hp) {
  def move(direction: Point) { position = position + direction }
}

object Agent {
  def createTurret(start: Point): Agent = new Agent("turret", ScreenChar('T'), start, 3)
}