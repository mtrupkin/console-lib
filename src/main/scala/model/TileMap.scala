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
    if (player.position == p) player.sc else tiles(p.x)(p.y).sc
  }

  def move(x: Int, y: Int): Boolean = ???

  def update(elapsed: Int) {
    size.foreach((x, y) => tiles(x)(y).update(elapsed))
  }

  //def wipe() {
    size.foreach((x, y) => {
      tiles(x)(y) match {
        case floor:FloorTile => floor.flipOn(y*1000 + 1000)
        case _ =>
      }
    })
  //}
}

trait Tile {
  var sc: ScreenChar = Tile.Floor
  def update(elapsed: Int)
}

object Tile {
  val Floor = ScreenChar('.', fg = RGBColor.LightGrey)
}

class FloorTile extends Tile {
  sc = Tile.Floor
  var anim: TileAnime = new SparkleAnime(Tile.Floor)

  def update(elapsed: Int) {
    sc = anim(elapsed)
  }

  def flipOn(delay: Int) {
    val a = new TempAnime(anim, new FrameAnime(Animations.flip, 250), 1000)

    anim = new DelayedAnime(anim, a, delay)
  }
}

class FlipFloorTile(oldChar: ScreenChar, newChar: ScreenChar) extends Tile {
  sc = oldChar
  def update(elapsed: Int) {}

}
