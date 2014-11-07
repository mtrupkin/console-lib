package console.controller

import console.app.{Game, Intro}
import console.core.Size
import console.control.LayoutOp._
import console.control._
import console.screen.{ConsoleKey, Screen}
import console.state.StateMachine

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

