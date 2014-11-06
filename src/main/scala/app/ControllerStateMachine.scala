package app

import model.World
import org.flagship.console.{Point, Size}
import org.flagship.console.control.LayoutOp._
import org.flagship.console.control._
import org.flagship.console.screen.{ConsoleKey, Screen}
import state.StateMachine

trait ControllerStateMachine extends StateMachine with Intro with Game {
  type StateType = ControllerState

  def initialSize: Size
  def initialState: ControllerState

  var size: Size = initialSize
  var screen = Screen(size)

  initialState.onEnter()

  def render(): Screen = {
    currentState.render(screen)
    screen
  }

  def keyPressed(key: ConsoleKey): Unit = {
    currentState.keyPressed(key: ConsoleKey)
  }

  def resize(newSize: Size): Unit = {
    size = newSize
  }

  trait ControllerState extends State {
     val window = new Composite("MainWindow", LayoutFlow.VERTICAL)
    window.layout = Layout(right = GRAB, bottom = GRAB)

    def addControl(control: Control): Unit = window.addControl(control)

    def update(elapsed: Int): Unit
    def render(screen: Screen): Unit = window.render(screen)
    def keyPressed(key: ConsoleKey): Unit

    override def onEnter(): Unit = {
      window.arrange(size)
      screen.clear()
    }
  }
}

object ControllerStateMachine extends ControllerStateMachine {
  lazy val initialSize: Size = Size(120, 42)
  lazy val initialState: ControllerState = new GameController(new World)
}
