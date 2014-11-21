package console.model

import console.screen.ScreenChar
import me.mtrupkin.geometry.Point

// Created: 4/8/2014

class Entity(
  val name: String,
  var position: Point,
  val sc: ScreenChar) {
}

class Player(name: String, p: Point, sc: ScreenChar)
  extends Entity(name, p, sc) {
  def move(p: Point) { position = position + p }
}