package me.mtrupkin.widget

import me.mtrupkin.console.control.Control
import me.mtrupkin.console.screen.{ConsoleKey, RGBColor, Screen}
import me.mtrupkin.geometry.{Point, Size}

/**
 * Created by mtrupkin on 11/29/2014.
 */

abstract class AbstractListWidget extends Control {
  def labels: List[String]

  var selected = 0
  val longestLabel: String = labels.reduceLeft((a, b) => if (a.length > b.length) a else b)

  override def minSize = Size(2 + longestLabel.length, labels.size)

  override def render(screen: Screen): Unit = {
    for ((label, i) <- labels.zipWithIndex) {
      val pad: Int = longestLabel.length - label.length + 1
      if (i == selected) {
        screen.fg = RGBColor.LightGrey
        screen.write(pad, i, s"$label ")
        screen.fg = RGBColor.White
      } else {
        screen.write(pad, i, s"$label ")
      }
    }
  }

  override def keyPressed(key: ConsoleKey): Unit = {
    import scala.swing.event.Key._
    key.keyValue match {
      case Up => selected -= 1
      case Down => selected += 1
      case Enter => select()
      case _ =>
    }
    val last = labels.length - 1
    if (selected < 0) selected = last else if ( selected > last ) selected = 0
  }

  override def mouseMoved(mouse: Point): Unit = {
    selected = mouse.y
  }

  override def mouseClicked(mouse: Point): Unit = {
    select()
  }

  def select(): Unit
}

class ListWidget(val items: List[(String, () => Unit)]) extends AbstractListWidget {
  lazy val labels = items.map(_._1)

  def select(): Unit = {
    items(selected)._2()
  }
}

class IndexListWidget(val labels: List[String], val action: (Int) => Unit) extends AbstractListWidget {
  def select(): Unit = {
    action(selected)
  }
}
