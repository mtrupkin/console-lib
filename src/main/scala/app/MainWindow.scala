package org.flagship.console.app

import org.flagship.console.control._
import org.flagship.console.control.LayoutOp._
import org.flagship.console.{Point, Size}
import org.flagship.console.screen.{Screen, ConsoleKey}
import map.MapWidget
import model.World


/**
 * Created by mtrupkin on 3/8/14.
 */
class MainWindow(size: Size) extends Composite("MainWindow", LayoutFlow.VERTICAL) {
  var time = 0
  var world = new World()
  layout = Layout(right = GRAB, bottom = GRAB)

  val topPanel = new Composite(name = "topPanel", layoutFlow = LayoutFlow.HORIZONTAL, border = Border.SINGLE_SANS_BOTTOM)
  topPanel.layout = Layout(right = GRAB, bottom = NONE)

  val bottomPanel = new Composite(name = "bottomPanel", border = Border.SINGLE_TEE_TOP)
  bottomPanel.layout = Layout(right = GRAB, bottom = GRAB)

  val mainPanel = new Composite(name = "mainPanel", border = Border.NONE)
  mainPanel.layout = Layout(right = GRAB, bottom = NONE)

  val mapPanel = new MapWidget(world)
  mainPanel.addControl(mapPanel)

  val detailBorder = new Border(box = Box.SINGLE_TEE_LEFT, BorderSides(right = false, top = false, bottom = false))

  val detailPanel = new Composite(name = "detailPanel", layoutFlow = LayoutFlow.VERTICAL, border = detailBorder)
  detailPanel.layout = Layout(right = GRAB, bottom = GRAB)

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
      case W | Up => world.player.move(Point.Up)
      case A | Left => world.player.move(Point.Left)
      case S | Down => world.player.move(Point.Down)
      case D | Right => world.player.move(Point.Right)
      case Enter => ???
      case Escape => ???
      case _ =>
      case _ =>
    }
  }

  override def update(elapsedTime: Int) {
    time += elapsedTime
    world.update(elapsedTime)
  }
}
