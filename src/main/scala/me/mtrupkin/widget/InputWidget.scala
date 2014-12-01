package me.mtrupkin.widget

import me.mtrupkin.console.control.Control
import me.mtrupkin.console.screen.{ConsoleKey, Screen}
import me.mtrupkin.geometry.Size

/**
 * Created by mtrupkin on 11/30/2014.
 */
class InputWidget(val label: String) extends Control {
  override def minSize: Size = Size(label.length + 2, 1)

  var value: String = ""

  override def render(screen: Screen): Unit = {
    screen.write(s"$label: $value")
  }

  override def keyPressed(key: ConsoleKey): Unit = {}
}
