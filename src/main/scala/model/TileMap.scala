package model

import org.flagship.console.screen.{RGBColor, ScreenChar}
import org.flagship.console.{Point, Size}
import scala.Array._

/**
 * Created by mtrupkin on 4/6/2014.
 */
trait TileMap {
  val size: Size
  def apply(p: Point): ScreenChar

  def move(x: Int, y: Int): Boolean

  def foreach(f: (Point, ScreenChar) => Unit ) {
    for (
      i <- 0 until size.width;
      j <- 0 until size.height
    ) {
      val p = Point(i, j)
      f(p, this(p))
    }
  }
}

class World extends TileMap {
  val size = Size(60, 30)
  val tiles = ofDim[Tile](size.width, size.height)
  val player: Player = new Player("player", Point(0,0), ScreenChar('@'))

  size.foreach((x, y) => tiles(x)(y) = new FloorTile())

  def apply(p: Point): ScreenChar = {
    if (player.position == p) player.sc else tiles(p.x)(p.y)()
  }

  def move(x: Int, y: Int): Boolean = ???

  def update(elapsed: Int) {
    size.foreach((x, y) => tiles(x)(y).update(elapsed))
  }

  //def wipe() {
    size.foreach((x, y) => {
      tiles(x)(y) match {
        case floor:FloorTile => floor.flipOn(y*1200)
        case _ =>
      }
    })
  //}
}

trait Tile {
  def apply(): ScreenChar
  def update(elapsed: Int)
}

class FloorTile extends Tile {
  import FloorTile._
  var state = Default
  var sc = Tile.Floor
  var totalTime = 0
  var delay = 0

  def apply(): ScreenChar = sc

  def update(elapsed: Int) = {
    state match {
      case Default => default(elapsed)
      case Flip => flip(elapsed)
      case _ =>
    }
  }

  def flipOn(delay0: Int) {
    delay = delay0
    totalTime = 0
    state = Flip
  }

  def flip(elapsed: Int) {

    totalTime += elapsed
    if (totalTime > delay ) {
      val slice = 100
      val timeSlice = (totalTime-delay) / slice

      timeSlice % 5 match {
        case 0 => sc = Flip1
        case 1 => sc = Flip2
        case 2 => sc = Flip3
        case 3 => sc = Flip4
        case 4 => sc = Flip1
      }

      if (totalTime > slice*10 + delay) {
        defaultOn()
      }
    }
  }

  def defaultOn() {
    totalTime = 0
    state = Default
    sc = Tile.Floor
  }

  def default(elapsed: Int) {
  }

  def animationOff() { state = None }
}

object FloorTile {
  val Default = "default"
  val Flip = "flip"
  val None = "node"

  val Flip1 = ScreenChar('\\', fg = RGBColor.LightGrey)
  val Flip2 = ScreenChar('|', fg = RGBColor.LightGrey)
  val Flip3 = ScreenChar('/', fg = RGBColor.LightGrey)
  val Flip4 = ScreenChar('-', fg = RGBColor.LightGrey)
}

object Tile {
  val Floor = ScreenChar('.', fg = RGBColor.LightGrey)
}
