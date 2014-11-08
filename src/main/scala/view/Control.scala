package console.control

import console.core.{Size, Point}
import console.screen.{ConsoleKey, Screen}


/**
 * User: mtrupkin
 * Date: 7/6/13
 */
trait Control {
  def minSize: Size
  var dimension: Size = Size.ONE

  var position = Point.Origin
  var layout: Layout = Layout.NONE

  def right: Int = position.x + dimension.width
  def bottom: Int = position.y + dimension.height

  def render(screen: Screen)

  def keyPressed(key: ConsoleKey): Unit = {}
  def mouseClicked(mouse: Point): Unit = {}
  def mouseMoved(mouse: Point): Unit = {}
  def update(elapsedTime: Int) {}

  def compact() {
    dimension = minSize
  }

  def grab(size: Size): Unit = {
    if (layout.right == LayoutOp.GRAB) {
      dimension = dimension.copy(width = size.width - position.x)
    }
    if (layout.bottom == LayoutOp.GRAB) {
      dimension = dimension.copy(height = size.height - position.y)
    }
  }

  def snap(size: Size) {
    if (layout.right == LayoutOp.SNAP) {
      position = position.copy(x = size.width - dimension.width)
    }
    if (layout.bottom == LayoutOp.SNAP) {
      position = position.copy(y = size.height - dimension.height)
    }
  }
}