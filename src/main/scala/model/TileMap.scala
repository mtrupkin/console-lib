package model

import org.flagship.console.screen.ScreenChar
import org.flagship.console.{Point, Size}

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

class World(val size: Size) extends TileMap {
  val player: Player = new Player("player", Point(0,0), ScreenChar('@'))

  def apply(p: Point): ScreenChar = {
    if (player.position == p) player.sc else Tile.Floor
  }
  def move(x: Int, y: Int): Boolean = ???
}

object Tile {
  val Floor = ScreenChar('.')
}
