package console.controller

import console.app.{Game, Intro}
import console.core.{Point, Size}
import console.control.LayoutOp._
import console.control._
import console.screen.{ConsoleKey, Screen}
import console.state.StateMachine


trait ControllerStateMachine extends StateMachine with Intro with Game {
  type StateType = ControllerState
  def initialSize: Size

  var size: Size = initialSize
  var screen = Screen(size)

  initialState.onEnter()

  def render(): Screen = {
    currentState.render(screen)
    screen
  }

  def keyPressed(key: ConsoleKey): Unit = {
    currentState.keyPressed(key)
  }

  def mouseMoved(mouse: Point): Unit = {
    currentState.mouseMoved(mouse)
  }

  def mouseExited(): Unit = currentState.mouseExited()

  def mouseEntered(): Unit = {
  }

  def mouseClick(mouse: Point): Unit = {
    currentState.mouseClick(mouse)
  }

  def resize(newSize: Size): Unit = {
    size = newSize
  }

  trait ControllerState extends State {
    val window = new Composite("MainWindow", LayoutFlow.VERTICAL)
    window.layout = Layout(right = GRAB, bottom = GRAB)
    var mouse: Option[Point] = None

    def addControl(control: Control): Unit = window.addControl(control)

    def update(elapsed: Int): Unit
    def render(screen: Screen): Unit = {
//      for (m <- mouse) {
//        screen.write(m.x, m.y, '+')
//      }
      window.render(screen)
    }

    def keyPressed(key: ConsoleKey): Unit = {
      window.keyPressed(key)
    }

    def mouseMoved(mouse: Point): Unit = {
      window.mouseMoved(mouse)

      clearMouse()
      this.mouse = Some(mouse)
    }

    def mouseClick(mouse: Point): Unit = {
      window.mouseClicked(mouse)
    }

    def mouseExited(): Unit = {
      clearMouse()
      this.mouse = None
    }

    def clearMouse(): Unit = {
      for (m <- this.mouse) {
        screen.clear(m.x, m.y)
      }
    }

    override def onEnter(): Unit = {
      window.arrange(size)
      screen.clear()
    }
  }
}

