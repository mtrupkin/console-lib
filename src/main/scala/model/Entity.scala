package model

import org.flagship.console.Point
import org.flagship.console.screen.ScreenChar

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