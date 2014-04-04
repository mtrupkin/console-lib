package org.flagship.console.control

import org.flagship.console.screen.{Screen, ASCII}
import org.flagship.console.{Point, Size}

// Created: 4/4/2014

case class Box(
  topLeft: Char, top: Char, topRight: Char,
  left: Char, right: Char,
  bottomLeft: Char, bottom: Char, bottomRight: Char)

object Box {
  val SINGLE = Box(
    ASCII.ULCORNER, ASCII.HLINE, ASCII.URCORNER,
    ASCII.VLINE, ASCII.VLINE,
    ASCII.LLCORNER, ASCII.HLINE, ASCII.LRCORNER)

  val DOUBLE = Box(
    ASCII.DOUBLE_LINE_UP_LEFT_CORNER, ASCII.DOUBLE_LINE_HORIZONTAL, ASCII.DOUBLE_LINE_UP_RIGHT_CORNER,
    ASCII.DOUBLE_LINE_VERTICAL, ASCII.DOUBLE_LINE_VERTICAL,
    ASCII.DOUBLE_LINE_LOW_LEFT_CORNER, ASCII.DOUBLE_LINE_HORIZONTAL, ASCII.DOUBLE_LINE_LOW_RIGHT_CORNER)


  val SINGLE_TEE_TOP = Box(
    ASCII.SINGLE_LINE_T_RIGHT, ASCII.HLINE, ASCII.SINGLE_LINE_T_LEFT,
    ASCII.VLINE, ASCII.VLINE,
    ASCII.LLCORNER, ASCII.HLINE, ASCII.LRCORNER)
}

case class BorderSides(left: Boolean = true, right: Boolean = true, top: Boolean = true, bottom: Boolean = true)

object BorderSides {
  val ALL = BorderSides()

  val SANS_TOP = BorderSides(top = false)
  val SANS_BOTTOM = BorderSides(bottom = false)
  val SANS_LEFT = BorderSides(left = false)
  val SANS_RIGHT = BorderSides(right = false)

  val NONE = BorderSides(left = false, right = false, top = false, bottom = false)
}

object Border {
  val SINGLE = new Border()
  val DOUBLE = new Border(Box.DOUBLE)
  val NONE = new Border(sides = BorderSides.NONE)

  val SINGLE_SANS_BOTTOM = new Border(sides = BorderSides.SANS_BOTTOM)
  val SINGLE_TEE_TOP = new Border(box = Box.SINGLE_TEE_TOP)
}

class Border(val box: Box = Box.SINGLE, val sides: BorderSides = BorderSides.ALL) {
  def borderSize: Size = {
    var width = 0
    var height = 0

    if (sides.left) width += 1
    if (sides.right) width += 1

    if (sides.top) height += 1
    if (sides.bottom) height += 1

    Size(width, height)
  }

  def borderOffset: Point = {
    var x = 0
    var y = 0

    if (sides.left) x += 1
    if (sides.top) y += 1

    Point(x, y)
  }

  def renderBorder(size: Size, screen: Screen, elementScreen: Screen) {
    import screen.write
    import size.{width, height}

    // sides
    if (sides.top) for(x <- 0 until width) write(x, 0, box.top)
    if (sides.bottom) for(x <- 0 until width) write(x, height - 1, box.bottom)


    if (sides.left) for(y <- 0 until height) write(0, y, box.left)
    if (sides.right) for(y <- 0 until height) write(0 + width - 1, y, box.right)

    // corners
    if (sides.top && sides.left) write(0, 0, box.topLeft)
    if (sides.top && sides.right) write(width - 1, 0, box.topRight)

    if (sides.bottom && sides.left) write(0, height - 1, box.bottomLeft)
    if (sides.bottom && sides.right) write(width - 1, height - 1, box.bottomRight)


    screen.display(borderOffset, elementScreen)
  }
}

