package org.flagship.console.control

import org.flagship.console.screen.Screen
import org.flagship.console.{Point, Size}
import org.flagship.console.control.LayoutFlow._

/**
 * S4i Systems Inc.
 * User: mtrupkin
 * Date: 7/8/13
 */
class Composite(val name: String, val layoutFlow: LayoutFlow.Value = HORIZONTAL) extends Control {
  val borderSize = Size(2, 2)

  def elementSize: Size = {
    var width = 0
    var height = 0

    if (controls != null) {
      controls.foreach(c => {
        if (c.right > width)
          width = c.right
        if (c.bottom > height)
          height = c.bottom} )
    }

    new Size(width, height)
  }

  override def minSize = elementSize.add(borderSize)


  var controls = List[Control]()

  def addControl(control: Control) {
    controls = controls :+ control
  }

  def render(screen: Screen) {
    controls.foreach(c => {
      val n = name
      val controlScreen = Screen(c.dimension)
      c.render(controlScreen)
      screen.display(c.position, controlScreen)
    })
  }

  def layout(size: Size) {
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
    var remaining: Size = Size(dimension.width, dimension.height)
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
    var remaining: Size = Size(dimension.width, dimension.height)
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
