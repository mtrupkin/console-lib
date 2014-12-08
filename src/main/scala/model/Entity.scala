package console.model

import me.mtrupkin.console.screen.ScreenChar
import me.mtrupkin.geometry.Point
import scala.util.Random

// Created: 4/8/2014

trait Entity extends Stats {
  val name: String
  def sc: ScreenChar
  def position: Point
  var hitPoints: Int
  def maxHitPoints: Int
}

class Agent(
  val name: String,
  val sc: ScreenChar,
  var position: Point,
  var str: Int = 0,
  val dex: Int = 0,
  val int: Int = 0) extends Entity {

  var hitPoints = maxHitPoints

  def act(p: Player): Unit = {
    Combat.attackRanged(this, p)
  }
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
  extends Agent (name, sc, p, 1, 1) {

  def move(direction: Point) = { position = position + direction }
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

  def str: Int
  def dex: Int
  def int: Int

  def melee: Combat = Combat( (str + floor(dex/2) + floor(int/3)).toInt)
  def ranged: Combat = Combat( (dex + floor(str/2) + floor(int/3)).toInt)

  def maxHitPoints = (str + dex + int) * 10
}

class Combat(f: => Int) {
  def attack: Int = f
  def damage: Int = f
  def defense: Int = f
}

object Combat {
  def apply(f: => Int): Combat = new Combat(f)

  def die(sides: Int): Int = Random.nextInt(sides) + 1

  def explode(sides: Int): Int = {
    var sum = 0
    var result = 0
    do {
      result = die(sides)
      sum += result
    } while(sides == result)
    sum
  }

  def roll(times: Int, sides: Int) = {
    val rolls = for {
      i <- 0 until times
    } yield die(sides)

    rolls.sum
  }

  def roll(): Int = roll(3, 6) // 3d6

  private def combat(attacker: Combat, defender: Combat): Int = {
    val attack = attacker.attack + roll()
    val defense = defender.defense + roll()

    val effect = attack - defense
    val damage = Math.max(attacker.damage, effect)

    println(s"(damage, base-damage, effect): ($damage, ${attacker.damage}, $effect)")

    damage
  }

  def attackMelee(attacker: Entity, defender: Entity): Unit = {
    val damage = combat(attacker.melee, defender.melee)
    defender.hitPoints = defender.hitPoints - damage
  }

  def attackRanged(attacker: Entity, defender: Entity): Unit = {
    val damage = combat(attacker.ranged, defender.ranged)
    defender.hitPoints = defender.hitPoints - damage
  }
}