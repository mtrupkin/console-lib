package org.flagship.console.app

import org.flagship.console.control.{Layout, LayoutFlow, Composite}
import org.flagship.console.control.LayoutOp._
import org.flagship.console.Size
import org.flagship.console.screen.ConsoleKey


/**
 * Created by mtrupkin on 3/8/14.
 */
class MainWindow(size: Size) extends Composite("MainWindow") {

  var time = 0

  val appPanel = new Composite(name = "appPanel", LayoutFlow.VERTICAL)
  appPanel.controlLayout = Layout(right = GRAB, bottom = GRAB)

  val topPanel = new Composite(name = "topPanel", LayoutFlow.HORIZONTAL)
  topPanel.controlLayout = Layout(right = GRAB, bottom = NONE)

  val bottomPanel = new Composite(name = "bottomPanel")
  bottomPanel.controlLayout = Layout(right = GRAB, bottom = SNAP)

  val mainPanel = new Composite(name = "mainPanel")
  mainPanel.controlLayout = Layout(right = SNAP, bottom = GRAB)

  val detailPanel = new Composite(name = "detailPanel")
  detailPanel.controlLayout = Layout(right = SNAP, bottom = GRAB)


  topPanel.addControl(mainPanel)
  topPanel.addControl(detailPanel)

  appPanel.addControl(topPanel)
  appPanel.addControl(bottomPanel)

  addControl(appPanel)

  layout(size)

  override def keyPressed(key: ConsoleKey) {
    import scala.swing.event.Key._

    val k = key.keyValue
    k match {
      case _ =>
    }
  }

  def accept() {
  }

  override def update(elapsedTime: Int) {
    time += elapsedTime
  }

}
