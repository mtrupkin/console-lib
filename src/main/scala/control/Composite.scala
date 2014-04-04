package org.flagship.console.control

import org.flagship.console.screen.{ASCII, Screen}
import org.flagship.console.{Point, Size}
import org.flagship.console.control.LayoutFlow._

/**
 * S4i Systems Inc.
 * User: mtrupkin
 * Date: 7/8/13
 */
class Composite(val name: String, val layoutFlow: LayoutFlow.Value = HORIZONTAL) extends Control {
  def elementSize: Size = {
    var width = 1
    var height = 1

    if (controls != null) {
      controls.foreach(c => {
        if (c.right > width)
          width = c.right
        if (c.bottom > height)
          height = c.bottom} )
    }

    new Size(width, height)
  }

  override def minSize = elementSize.add(Border.borderSize)


  var controls = List[Control]()

  def addControl(control: Control) {
    controls = controls :+ control
  }

  def render(screen: Screen) {
    Border.render(dimension, Box.DOUBLE, screen)
    val elementScreen = Screen(elementSize)

    val tmp = name

    controls.foreach(c => {
      val n = name
      val controlScreen = Screen(c.dimension)
      c.render(controlScreen)
      elementScreen.display(c.position, controlScreen)
    })
    screen.display(Point(1,1), elementScreen)
  }

  def arrange(size: Size) {
    compact()

    grab(size)
    snap(size)
  }

  override def compact() {
    controls.foreach( c => c.compact() )

    layoutFlow match {
      case HORIZONTAL => hFlow(controls)
      case VERTICAL => vFlow(controls)
    }

    super.compact()
  }

  override def snap(size: Size) {
    super.snap(size)
    //layoutManager.snap(dimension, controls)
    var remaining: Size = Size(dimension.width, dimension.height).subtract(Border.borderSize)
    if (layoutFlow == HORIZONTAL) {

      for (c <- controls.reverse) {
        c.snap(remaining)
        remaining = Size(remaining.width - c.dimension.width, remaining.height)
      }
    } else if (layoutFlow ==  VERTICAL) {
      var remaining: Size = Size(size.width, size.height)
      for (c <- controls.reverse) {
        c.snap(remaining)
        remaining = Size(remaining.width, remaining.height - c.dimension.height)
      }
    }
  }

  override def grab(size: Size): Unit = {
    super.grab(size)
    //layoutManager.grab(dimension, controls)
    var remaining: Size = Size(dimension.width, dimension.height).subtract(Border.borderSize)

    if (layoutFlow == HORIZONTAL) {
      for(c <- controls.reverse) {
        c.grab(remaining)
        remaining = Size(remaining.width - c.dimension.width, remaining.height)
      }
    } else if (layoutFlow == VERTICAL) {
      for(c <- controls.reverse) {
        c.grab(remaining)
        remaining = Size(remaining.width, remaining.height - c.dimension.height)
      }
    }
  }


  override def mouseClicked(mouse: Point) {
    val m = new Point(mouse.x - position.x, mouse.y - position.y)
    for( c <- controls ) {
      if (in(c, m)) {c.mouseClicked(m)}
    }
  }

  def in(c: Control, p: Point): Boolean = {
    (c.position.x >= p.x && c.position.y >= p.y &&
      c.position.x + c.dimension.width <= p.x &&
      c.position.y + c.dimension.height <= p.y)
  }

  def hFlow(controls: List[Control]) {
    var lastPos = Point.Origin
    for (c <- controls) {
      c.position = lastPos
      lastPos = Point(c.right, 0)
    }
  }

  def vFlow(controls: List[Control]) {
    var lastPos = Point.Origin
    for (c <- controls) {
      c.position = lastPos
      lastPos = Point(0, c.bottom)
    }
  }
}

object Border {
  val borderSize = Size(2, 2)

  def render(size: Size, box: Box, screen: Screen) {
    import screen.write
    import size.{width, height}

    // corners
    write(0, 0, box.topLeft)
    write(width - 1, 0, box.topRight)
    write(0, height - 1, box.bottomLeft)
    write(width - 1, height - 1, box.bottomRight)

    // sides
    for(x <- 1 until width-1 ) {
      write(x, 0, box.top)
      write(x, height - 1, box.bottom)
    }

    for(y <- 1 until height-1 ) {
      write(0, y, box.left)
      write(0 + width - 1, y, box.right)
    }
  }
}

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
}
