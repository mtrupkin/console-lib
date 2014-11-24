package me.mtrupkin.console.control

import console.screen.{ConsoleKey, Screen}
import me.mtrupkin.geometry.{Point, Size}
import me.mtrupkin.console.layout.{Layout, Fill, Pos, HPos, VPos}

/**
 * User: mtrupkin
 * Date: 7/6/13
 */
trait Control {
  def minSize: Size

  var dimension: Size = Size.ONE
  var position = Point.Origin
  var layout: Option[Layout] = None

  def right: Int = position.x + dimension.width
  def bottom: Int = position.y + dimension.height

  def render(screen: Screen): Unit

  def keyPressed(key: ConsoleKey): Unit = {}
  def mouseClicked(mouse: Point): Unit = {}
  def mouseMoved(mouse: Point): Unit = {}
  def update(elapsedTime: Int) {}

  def compact() {
    dimension = minSize
  }

  def grow(fill: Fill, size: Size): Unit = {
    import me.mtrupkin.console.layout.HFill._
    import me.mtrupkin.console.layout.VFill._

    for(hFill <- fill.hFill) {
      hFill match {
        case RIGHT => dimension = dimension.copy(width = size.width - position.x)
      }
    }

    for(vFill <- fill.vFill) {
      vFill match {
        case BOTTOM => dimension = dimension.copy(height = size.height - position.y)
      }
    }
  }

  def grab(size: Size): Unit = {
    for {
      layout <- layout
      fill <- layout.fill
    } {
      grow(fill, size)
    }
  }

  def align(pos: Pos, size: Size): Unit = {
    import me.mtrupkin.console.layout.HPos._
    import me.mtrupkin.console.layout.VPos._

    for(hPos <- pos.hPos) {
      hPos match {
        case LEFT => position = position.copy(x = 0)
        case HPos.CENTER => position = position.copy(x = (size.width - dimension.width) / 2)
        case RIGHT => position = position.copy(x = size.width - dimension.width)
      }
    }

    for(vPos <- pos.vPos) {
      vPos match {
        case TOP => position = position.copy(y = 0)
        case VPos.CENTER => position = position.copy(y = (size.height - dimension.height) / 2)
        case BOTTOM => position = position.copy(y = size.height - dimension.height)
      }
    }
  }

  def snap(size: Size) {
    for {
      layout <- layout
      pos <- layout.pos
    } {
      align(pos, size)
    }
  }
}