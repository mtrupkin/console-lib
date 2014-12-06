package console.model

import me.mtrupkin.console.screen.ScreenChar
import me.mtrupkin.geometry.Point

// Created: 4/8/2014

trait Entity extends Stats {
  val name: String
  def sc: ScreenChar
  def position: Point
  def hitPoints: Int
  def maxHitPoints: Int
}

class Agent(
  val name: String,
  val sc: ScreenChar,
  var position: Point) extends Entity {
  var hitPoints: Int = maxHitPoints
}

object Agent {
  def apply(name: String, sc: ScreenChar, p: Point, hp: Int): Agent = {
    val a = new Agent(name, sc, p)
    a.hitPoints = hp
    a
  }

  def createTurret(start: Point): Agent = new Agent("turret", ScreenChar('T'), start)
}

class Player(name: String, sc: ScreenChar, p: Point)
  extends Agent (name, sc, p) {
  def move(direction: Point) { position = position + direction }
}

object Player {
  def apply(name: String, sc: ScreenChar, p: Point): Player = new Player(name, sc, p)
  def apply(name: String, sc: ScreenChar, p: Point, hp: Int): Player = {
    val a = new Player(name, sc, p)
    a.hitPoints = hp
    a
  }
}

trait Stats {
  import Math.floor

  def str: Int = 0
  def dex: Int = 0
  def int: Int = 0

  def melee: Combat = Combat( () => (str + floor(dex/2) + floor(int/3)).toInt)
  def range: Combat = Combat( () => (dex + floor(str/2) + floor(int/3)).toInt)

  def maxHitPoints = str + dex + int
}

class Combat(f: () => Int) {
  def attack(): Int = f()
  def damage(): Int = f()
  def defense(): Int = f()
}

object Combat {
  def apply(f: () => Int): Combat = new Combat(f)

  def die(): Int = (Math.random() * 6).toInt
  def roll() = die() + die() + die()

  def combat(attacker: Combat, defender: Combat): Int = {
    val attack = attacker.attack + roll()
    val defense = defender.defense + roll()

    val effect = attack - defense
    val damage = attacker.damage()

    println(s"(damage, effect): ($damage, $effect)")

    if (effect < 0) damage else damage + effect
  }
}