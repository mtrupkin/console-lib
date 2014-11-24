package me.mtrupkin.tile

import console.model._
import console.screen.{RGBColor, ScreenChar}
import me.mtrupkin.geometry.{Point, Size}

import scala.Array._

/**
 * Created by mtrupkin on 11/22/2014.
 */
trait Tile {
  def sc: ScreenChar
  def update(elapsed: Int)
  def move: Boolean
}

trait TileMap {
  val size: Size
  def apply(x: Int, y: Int): Tile
  def foreach(f: (Int, Int, Tile) => Unit ) = size.foreach((x,y) => f(x, y, this(x, y)))
  def update(elapsed: Int): Unit = size.foreach((x, y) => this(x, y).update(elapsed))

  def wipe() {
    size.foreach((x, y) => {
      this(x, y) match {
        case floor:Floor => floor.flipOn(y*100)
        case _ =>
      }
    })
  }
}

class TileMapImpl(val size: Size) extends TileMap {
  val tiles = ofDim[Tile](size.width, size.height)
  def apply(x: Int, y: Int): Tile = tiles(x)(y)

}

class Floor extends Tile {
  var sc = ScreenChar(' ', fg = RGBColor.LightGrey)
  var anim: TileAnime = new SparkleAnime(ScreenChar(' ', fg = RGBColor.LightGrey))
  val move = true
  def update(elapsed: Int) {
    sc = anim(elapsed)
  }

  def flipOn(delay: Int) {
    val a = new TempAnime(anim, new FrameAnime(Animations.flip, 20), 120)

    anim = new DelayedAnime(anim, a, delay)
  }
}

class Wall(val sc: ScreenChar) extends Tile {
  val move = false
  def update(elapsed: Int) = {}
}
