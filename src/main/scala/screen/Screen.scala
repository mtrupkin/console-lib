package org.flagship.console.screen

import org.flagship.console.{Point, Size}
import org.flagship.console.screen.RGBColor._

/**
 * User: mtrupkin
 * Date: 7/5/13
 */
class Screen(size: Size) {
  def width: Int = size.width
  def height: Int = size.height

  var fg = White
  var bg = Black
  var cursor = Point.Origin
  val buffer = Array.ofDim[ScreenChar](size.width, size.height)

  clear()

  def clear() = foreach((p, s) => this(p) = ScreenChar.Blank)

  def apply(x: Int, y: Int): ScreenChar = {
    buffer(x)(y)
  }

  def apply(p: Point): ScreenChar = {
    this(p.x, p.y)
  }

  def update(x: Int, y: Int, sc: ScreenChar ) {
    buffer(x)(y) = sc
  }

  def update(p: Point, sc: ScreenChar) {
    update(p.x, p.y, sc)
  }

  def foreach(f: (Point, ScreenChar) => Unit ) {
    for (
      i <- 0 until size.width;
      j <- 0 until size.height
    ) f(Point(i, j), this(i, j))
  }

  def move(x: Int, y: Int) {
    cursor = Point(x, y)
  }

  def write(c: Char) {
    this(cursor.x, cursor.y) = ScreenChar(c, fg, bg)
  }

  def write(x: Int, y: Int, c: Char) {
    this(x, y) = ScreenChar(c, fg, bg)
  }

  def write(x: Int, y: Int, c: Char, fg0: RGBColor, bg0: RGBColor) {
    this(x, y) = ScreenChar(c, fg0, bg0)
  }

  def write(s: String) {
    write(0, 0, s)
  }

  def write(x: Int, y: Int, s: String) {
    var pos = x
    s.foreach( c => { write(pos, y, c); pos += 1 } )
  }

  def display(x: Int, y: Int, screen: Screen): Unit = display(Point(x, y), screen)
  def display(p: Point, screen: Screen): Unit = screen.foreach((p0, s) => this(p + p0) = s)
}

object Screen {
  def apply(size: Size) = new Screen(size)
}

case class ScreenChar(c: Char, fg: RGBColor = White, bg: RGBColor = Black)

object ScreenChar {
  val Blank = ScreenChar(' ')
}
