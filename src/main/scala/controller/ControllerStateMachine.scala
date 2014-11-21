package console.controller

import console.app.{Game, Intro}
import me.mtrupkin.console.control._
import console.screen.{ConsoleKey, Screen}
import console.state.StateMachine
import me.mtrupkin.console.layout.{Orientation, Fill, Pos, Layout}
import me.mtrupkin.geometry.{Point, Size}
import me.mtrupkin.terminal.Input


trait ControllerStateMachine extends StateMachine with Intro with Game {
  type StateType = ControllerState
  def initialSize: Size

  var size: Size = initialSize
  var screen = Screen(size)
  var closed = false

  initialState.onEnter()

  def render(): Screen = {
    currentState.render(screen)
    screen
  }

  def handle(input: Input): Unit = {
    currentState.handle(input)
  }

  def resize(newSize: Size): Unit = {
    size = newSize
  }

  trait ControllerState extends State {
    val window: Composite
    var mouse: Option[Point] = None

    def update(elapsed: Int): Unit

    def render(screen: Screen): Unit = {
      window.render(screen)
    }

    def handle(input: Input): Unit = {
      for (key <- input.key) {
        window.keyPressed(key)
      }

      for (mouse <- input.mouseMove) {
        window.mouseMoved(mouse)
      }

      for (mouse <- input.mouseClick) {
        window.mouseClicked(mouse)
      }

      for (mouse <- input.mouseExit) {
      }
    }

    override def onEnter(): Unit = {
      window.arrange(size)
      screen.clear()
    }
  }
}

