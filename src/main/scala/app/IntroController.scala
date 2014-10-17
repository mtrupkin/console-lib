package app

import model.World
import org.flagship.console.Size
import org.flagship.console.control.LayoutOp._
import org.flagship.console.control.{Layout, LayoutFlow, Composite, Control}
import org.flagship.console.screen.{ConsoleKey, Screen}
import org.flagship.game.{GameEngine, Controller}
import state.StateMachine

// Created: 10/17/2014

class IntroController extends Controller {

  var elapsed = 0
  val mainWindow = new Composite("MainWindow", LayoutFlow.VERTICAL)
  mainWindow.layout = Layout(right = GRAB, bottom = GRAB)

  val label1 = new Control {
    override def minSize = Size(20, 1)
    override def render(screen: Screen): Unit = {
      screen.write(s"${elapsed}")
    }
  }

  mainWindow.addControl(label1)

  // XXX
  var newGame = false

  def newGame(machine: GameEngine): Unit = {
    val world = new World()
    val controller = new GameController(world)

    machine.changeState(controller)
  }

  override def update(machine: GameEngine, elapsed: Int): Unit = {
    this.elapsed += elapsed
    if (newGame) { newGame(machine)}
  }

  override def keyPressed(key: ConsoleKey): Unit = {
    newGame = true
  }

  override def render(screen: Screen): Unit = mainWindow.render(screen)
}