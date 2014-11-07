package console.control

import console.core.{Size, Point}
import console.screen.{Screen, ASCII}

// Created: 4/4/2014

case class BoxSide(side: Int)

object BoxSide {
  val TOP_LEFT = BoxSide(0)
  val TOP = BoxSide(1)
  val TOP_RIGHT = BoxSide(2)
  val RIGHT = BoxSide(3)
  val BOTTOM_RIGHT = BoxSide(4)
  val BOTTOM = BoxSide(5)
  val BOTTOM_LEFT = BoxSide(6)
  val LEFT = BoxSide(7)
}

case class Box(
  topLeft: Char, top: Char, topRight: Char,
  left: Char, right: Char,
  bottomLeft: Char, bottom: Char, bottomRight: Char)

case class Divider(single: Boolean)

object Divider {
  val SINGLE = Some(Divider(true))
  val DOUBLE = Some(Divider(false))
}

object Box {
  import ASCII._


  def side(box:Box, side: BoxSide): Char = {
    import BoxSide._

    side match {
      case TOP_LEFT => box.topLeft
      case TOP => box.top
      case TOP_RIGHT => box.topRight
      case RIGHT => box.right
      case BOTTOM_RIGHT => box.bottomRight
      case BOTTOM => box.bottom
      case BOTTOM_LEFT => box.bottomLeft
      case LEFT => box.left
    }
  }

  val SINGLE = Box(
    ULCORNER, HLINE, URCORNER,
    VLINE, VLINE,
    LLCORNER, HLINE, LRCORNER)

  val DOUBLE = Box(
    DOUBLE_ULCORNER, DOUBLE_HLINE, DOUBLE_URCORNER,
    DOUBLE_VLINE, DOUBLE_VLINE,
    DOUBLE_LLCORNER, DOUBLE_HLINE, DOUBLE_LRCORNER)

  val SINGLE_TEE_TOP = Box(
    SINGLE_LINE_T_RIGHT, HLINE, SINGLE_LINE_T_LEFT,
    VLINE, VLINE,
    LLCORNER, HLINE, LRCORNER)

  val SINGLE_TEE_LEFT = Box(
    SINGLE_LINE_T_LEFT, HLINE, URCORNER,
    VLINE, VLINE,
    SINGLE_LINE_T_LEFT, HLINE, LRCORNER)

  val SINGLE_TEE_BOTTOM = Box(
    ULCORNER, HLINE, URCORNER,
    VLINE, VLINE,
    SINGLE_LINE_T_RIGHT, HLINE, SINGLE_LINE_T_LEFT)

  def getTee(single: Boolean, boxSide: BoxSide, box: Box): Char = {
    if (single) getSingleTee(boxSide, box) else getDoubleTee(boxSide, box)
  }

  def getSingleTee(boxSide: BoxSide, box: Box): Char = {
    boxSide match {
      case BoxSide.TOP => box.top match {
        case HLINE => SINGLE_LINE_T_DOWN
        case DOUBLE_HLINE => DOUBLE_LINE_T_SINGLE_DOWN
      }
      case BoxSide.BOTTOM => box.bottom match {
        case HLINE => SINGLE_LINE_T_UP
        case DOUBLE_HLINE => DOUBLE_LINE_T_SINGLE_UP
      }
      case BoxSide.LEFT => box.left match {
        case VLINE => SINGLE_LINE_T_DOUBLE_RIGHT
        case DOUBLE_VLINE => DOUBLE_LINE_T_RIGHT
      }
      case BoxSide.RIGHT => box.right match {
        case HLINE => SINGLE_LINE_T_DOUBLE_LEFT
        case DOUBLE_HLINE => DOUBLE_LINE_T_LEFT
      }
    }
  }

  def getDoubleTee(boxSide: BoxSide, box: Box): Char = {
    boxSide match {
      case BoxSide.TOP => box.top match {
        case HLINE => SINGLE_LINE_T_DOUBLE_DOWN
        case DOUBLE_HLINE => DOUBLE_LINE_T_DOWN
      }
      case BoxSide.BOTTOM => box.bottom match {
        case HLINE => SINGLE_LINE_T_DOUBLE_UP
        case DOUBLE_HLINE => DOUBLE_LINE_T_UP
      }
      case BoxSide.LEFT => box.left match {
        case VLINE => SINGLE_LINE_T_DOUBLE_RIGHT
        case DOUBLE_VLINE => DOUBLE_LINE_T_RIGHT
      }
      case BoxSide.RIGHT => box.right match {
        case HLINE => SINGLE_LINE_T_DOUBLE_LEFT
        case DOUBLE_HLINE => DOUBLE_LINE_T_LEFT
      }
    }
  }
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

  val SINGLE_SANS_TOP = new Border(sides = BorderSides.SANS_TOP)
  val SINGLE_SANS_BOTTOM = new Border(sides = BorderSides.SANS_BOTTOM)
  val SINGLE_TEE_TOP = new Border(box = Box.SINGLE_TEE_TOP)
  val SINGLE_TEE_BOTTOM = new Border(box = Box.SINGLE_TEE_BOTTOM)
}

class Border(val box: Box = Box.SINGLE, val sides: BorderSides = BorderSides.ALL, val divider: Option[Divider] = None) {
  def borderSize: Size = {
    var width = 0
    var height = 0

    if (sides.left) width += 1
    if (sides.right) width += 1

    if (sides.top) height += 1
    if (sides.bottom) height += 1

    Size(width, height)
  }

  def dividerSize: Size = {
    divider match {
      case Some(d) => Size(1, 0)
      case None => Size.ZERO
    }
  }

  def borderOffset: Point = {
    val x = if (sides.left) 1 else 0
    val y = if (sides.top) 1 else 0

    Point(x, y)
  }

  def renderVerticalDivider(screen: Screen, control: Control) {
    for( d <- divider ) {
      import screen.write
      import control._

      val divider = if (d.single) ASCII.VLINE else ASCII.DOUBLE_VLINE

      for (yy <- 0 until dimension.height) {
        write(position.x + dimension.width, position.y + yy, divider)
      }

      if (sides.top) {
        val top = Box.getTee(d.single, BoxSide.TOP, this.box)
        write(position.x + dimension.width, position.y - 1, top)
      }

      if (sides.bottom) {
        val bottom = Box.getTee(d.single, BoxSide.BOTTOM, this.box)
        write(position.x + dimension.width, position.y + dimension.height, bottom)
      }
    }
  }

  def renderBorder(size: Size, screen: Screen) {
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
  }
}

