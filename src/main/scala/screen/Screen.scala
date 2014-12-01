package me.mtrupkin.console.screen

import me.mtrupkin.console.screen.RGBColor._
import me.mtrupkin.geometry.{Point, Size}

/**
 * User: mtrupkin
 * Date: 7/5/13
 */

trait Screen {
  val size: Size
  var fg = White
  var bg = Black

  def apply(x: Int, y: Int): ScreenChar
  def update(x: Int, y: Int, sc: ScreenChar )

  def clear() = foreach((x, y, s) => this(x, y) = ScreenChar.Blank)
  def clear(x: Int, y: Int) = this(x, y) = ScreenChar.Blank

  def foreach(f: (Int, Int, ScreenChar) => Unit ): Unit = {
    for (
      i <- 0 until size.width;
      j <- 0 until size.height
    ) f(i, j, this(i, j))
  }

  def transform(f: (ScreenChar) => ScreenChar ): Unit = {
    for (
      i <- 0 until size.width;
      j <- 0 until size.height
    ) this(i, j) = f(this(i, j))
  }

  def write(p: (Int, Int), c: Char): Unit = {
    write(p._1, p._2, c)
  }

  def write(x: Int, y: Int, c: Char): Unit = {
    this(x, y) = ScreenChar(c, fg, bg)
  }
  def write(s: String): Unit = write(0, 0, s)

  def subScreen(origin: Point, size: Size): Screen = new SubScreen(origin, size, this)

  def write(x: Int, y: Int, s: String): Unit = {
    var pos = x
    s.foreach( c => { write(pos, y, c); pos += 1 } )
  }
}

class SubScreen(val origin: Point, val size: Size, val screen: Screen) extends Screen {
  def update(x: Int, y: Int, sc: ScreenChar) = {
    screen.update(origin.x + x, origin.y + y, sc)
  }

  def apply(x: Int, y: Int): ScreenChar = {
    screen(origin.x + x, origin.y + y)
  }
}

object Screen {
  def apply(size: Size):Screen = new RootScreen(size)
}

class RootScreen(val size: Size) extends Screen {
  val buffer = Array.ofDim[ScreenChar](size.width, size.height)
  clear()

  def apply(x: Int, y: Int): ScreenChar = {
    buffer(x)(y)
  }

  def update(x: Int, y: Int, sc: ScreenChar ) {
    buffer(x)(y) = sc
  }
}


case class ScreenChar(c: Char, fg: RGBColor = White, bg: RGBColor = Black)

object ScreenChar {
  val Blank = ScreenChar(' ')
}
