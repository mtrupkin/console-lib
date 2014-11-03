package app

import org.flagship.console.control._
import org.flagship.console.control.LayoutOp._
import map.MapWidget
import org.flagship.console.{Point, Size}
import org.flagship.console.screen.{ConsoleKey, Screen}
import model.World
import org.flagship.game.ViewStateMachine

// Created: 9/18/2014

class GameController(val world: World) extends ViewStateMachine.Controller {

  val size = Size(120, 42)

  val mainWindow = new Composite("MainWindow", LayoutFlow.VERTICAL)
  mainWindow.layout = Layout(right = GRAB, bottom = GRAB)

  val topPanel = new Composite(name = "topPanel", layoutFlow = LayoutFlow.HORIZONTAL, border = new Border(box = Box.SINGLE_TEE_BOTTOM, divider = Divider.DOUBLE))//border = Border.SINGLE)
  topPanel.layout = Layout(right = GRAB, bottom = NONE)

  val bottomPanel = new Composite(name = "bottomPanel", border = Border.SINGLE_SANS_TOP)
  bottomPanel.layout = Layout(right = GRAB, bottom = GRAB)

  val mainBorder = new Border(box = Box.DOUBLE, sides = BorderSides(left = false, top = false, bottom = false))
  val mainPanel = new Composite(name = "mainPanel")
  mainPanel.layout = Layout(right = NONE, bottom = NONE)

  val mapPanel = new MapWidget(world) {
    def onSelection = {

    }
  }
  mainPanel.addControl(mapPanel)

  val detailBorder = new Border(box = Box.DOUBLE, sides = BorderSides(right = false, top = false, bottom = false))
  val detailPanel = new Composite(name = "detailPanel", layoutFlow = LayoutFlow.VERTICAL)
  detailPanel.layout = Layout(right = GRAB, bottom = GRAB)


  val label1 = new Control {
    override def minSize = Size(20, 1)
    override def render(screen: Screen): Unit = {
      screen.write(s"${world.time}")
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

  mainWindow.addControl(topPanel)
  mainWindow.addControl(bottomPanel)

  mainWindow.arrange(size)

  def update(elapsed: Int) {
    world.update(elapsed)

    if (endGame) {
      changeState(new IntroController)
    }
  }

  def render(screen: Screen) {
    mainWindow.render(screen)
  }

  //
  var endGame = false

  def keyPressed(key: ConsoleKey) {
    mainWindow.keyPressed(key)

    import scala.swing.event.Key._

    val k = key.keyValue
    k match {
      case W | Up => world.player.move(Point.Up)
      case A | Left => world.player.move(Point.Left)
      case S | Down => world.player.move(Point.Down)
      case D | Right => world.player.move(Point.Right)
      case Enter => ???
      case Escape => endGame = true
      case _ =>
      case _ =>
    }
  }
}
