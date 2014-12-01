package me.mtrupkin.console.control

import me.mtrupkin.console.screen.Screen
import me.mtrupkin.console.screen.{ConsoleKey}
import me.mtrupkin.geometry.{Point, Size}
import me.mtrupkin.console.layout.Orientation


/**
 * S4i Systems Inc.
 * User: mtrupkin
 * Date: 7/8/13
 */
class Composite(val name: String, val layoutFlow: Orientation.EnumVal = Orientation.HORIZONTAL, val border: Border = Border.NONE) extends Control {
  def elementSize: Size = {
    var width = 1
    var height = 1

    def adjustSize(c: Control, divider: Size ) {
      val right = c.right + divider.width
      if (right > width)
        width = right
      val bottom = c.bottom + divider.height
      if (bottom > height)
        height = bottom
    }

    def elementSize(controls: List[Control]) {
      controls match {
        case c::cs => {
          adjustSize(c, border.dividerSize)
          elementSize(cs)
        }
        case c::Nil => adjustSize(c, Size.ZERO)
        case Nil =>
      }
    }
    elementSize(controls)

    new Size(width, height)
  }

  override def minSize = elementSize.add(border.borderSize)

  var controls = List[Control]()

  def addControl(control: Control) {
    controls = controls :+ control
  }

  def addControls(controls: Seq[Control]) {
    this.controls = this.controls ++ controls
  }

  def render(screen: Screen) {
    border.renderBorder(dimension, screen)
    val elementScreen = screen.subScreen(border.borderOffset, elementSize)

    if (controls.length>0) {
      for (c <- controls.init) {
        val controlScreen = elementScreen.subScreen(c.position, c.dimension)
        c.render(controlScreen)

        for(d <- border.divider) {
          border.renderVerticalDivider(elementScreen, c)
        }
      }

      val c = controls.last
      val controlScreen = elementScreen.subScreen(c.position, c.dimension)
      c.render(controlScreen)
    }
  }

  def arrange(size: Size) {
    compact()

    grab(size)
    snap(size)
  }

  override def compact() {
    controls.foreach( c => c.compact() )

    layoutFlow match {
      case Orientation.HORIZONTAL => hFlow(controls)
      case Orientation.VERTICAL => vFlow(controls)
    }

    super.compact()
  }

  override def snap(size: Size) {
    super.snap(size)
    //layoutManager.snap(dimension, controls)
    var remaining: Size = Size(dimension.width, dimension.height).subtract(border.borderSize)
    if (layoutFlow == Orientation.HORIZONTAL) {
      for (c <- controls.reverse) {
        c.snap(remaining)
        remaining = Size(remaining.width - c.dimension.width, remaining.height)
      }
    } else if (layoutFlow ==  Orientation.VERTICAL) {
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
    var remaining: Size = Size(dimension.width, dimension.height).subtract(border.borderSize)

    if (layoutFlow == Orientation.HORIZONTAL) {
      for(c <- controls.reverse) {
        c.grab(remaining)
        remaining = Size(remaining.width - c.dimension.width, remaining.height)
      }
    } else if (layoutFlow == Orientation.VERTICAL) {
      for (d <- border.divider) {
        remaining = remaining.copy(height = remaining.height - Math.max(0, controls.length-1))
      }
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

  override def mouseMoved(mouse: Point) {
    val m = new Point(mouse.x - position.x, mouse.y - position.y)
    for( c <- controls ) {
      if (in(c, m)) {c.mouseMoved(m - border.borderOffset)}
    }
  }

  override def keyPressed(key: ConsoleKey): Unit = {
    for( c <- controls ) {
      c.keyPressed(key)
    }
  }

  def in(c: Control, p: Point): Boolean = {
    val cp = c.position + border.borderOffset

    p.x >= cp.x &&
      p.x < cp.x + c.dimension.width &&
      p.y >= cp.y &&
      p.y < cp.y + c.dimension.height
  }

  def hFlow(controls: List[Control]) {
    val dividerWidth = border.divider match {
      case Some(d) => 1
      case None => 0
    } 

    var lastPos = Point.Origin
    for (c <- controls) {
      c.position = lastPos
      lastPos = Point(c.right + dividerWidth, 0)
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
