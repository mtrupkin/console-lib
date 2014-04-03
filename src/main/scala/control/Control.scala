package org.flagship.console.control

import org.flagship.console.control.{LayoutOp, Layout}
import org.flagship.console._
import org.flagship.console.screen._

/**
 * User: mtrupkin
 * Date: 7/6/13
 */
trait Control {
  def minSize: Size = Size(1, 1)
  var dimension: Size = minSize

  var position = Point.Origin
  var layout: Layout = Layout.NONE

  def right: Int = position.x + dimension.width
  def bottom: Int = position.y + dimension.height

  def render(screen: Screen)

  def keyPressed(key: ConsoleKey) {}
  def mouseClicked(mouse: Point) {}
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