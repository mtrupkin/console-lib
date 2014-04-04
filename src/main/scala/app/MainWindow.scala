package org.flagship.console.app

import org.flagship.console.control.{Control, Layout, LayoutFlow, Composite}
import org.flagship.console.control.LayoutOp._
import org.flagship.console.Size
import org.flagship.console.screen.{Screen, ConsoleKey}


/**
 * Created by mtrupkin on 3/8/14.
 */
class MainWindow(size: Size) extends Composite("MainWindow", LayoutFlow.VERTICAL) {
  var time = 0
  layout = Layout(right = GRAB, bottom = GRAB)

  val topPanel = new Composite(name = "topPanel", LayoutFlow.HORIZONTAL)
  topPanel.layout = Layout(right = GRAB, bottom = NONE)

  val bottomPanel = new Composite(name = "bottomPanel")
  bottomPanel.layout = Layout(right = GRAB, bottom = GRAB)

  val mainPanel = new Composite(name = "mainPanel")
  mainPanel.layout = Layout(right = GRAB, bottom = NONE)

  val mapPanel = new Control {
    override def minSize = Size(20, 10)
    override def render(screen: Screen): Unit = {screen.write("@ t i")}
  }
  mainPanel.addControl(mapPanel)

  val detailPanel = new Composite(name = "detailPanel", LayoutFlow.HORIZONTAL)
  detailPanel.layout = Layout(right = GRAB, bottom = NONE)
  val label1 = new Control {
    override def minSize = Size(20, 1)
    override def render(screen: Screen): Unit = {
      screen.write("label 1")
    }
  }
  val label2 = new Control {
    override def minSize = Size(20, 1)
    override def render(screen: Screen): Unit = {
      screen.write("label 2")
    }
  }
  val label3 = new Control {
    override def minSize = Size(20, 1)
    override def render(screen: Screen): Unit = {
      screen.write("label 3")
    }
  }
  detailPanel.addControl(label1)
  detailPanel.addControl(label2)
  detailPanel.addControl(label3)



  topPanel.addControl(mainPanel)
  topPanel.addControl(detailPanel)

  addControl(topPanel)
  addControl(bottomPanel)

  arrange(size)

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
