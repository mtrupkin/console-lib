package console.app

/**
 * User: mtrupkin
 * Date: 11/29/13
 */

import console.core.Size
import console.engine.GameEngine
import console.model.World
import console.terminal.SwingTerminal
import console.controller.ControllerStateMachine


// TODO: fix right grab for vertical layout
object GameApp extends App {
  lazy val size = Size(120, 42)
  val terminal = new SwingTerminal(size, "Danger Room")

  object ControllerStateMachine extends ControllerStateMachine {
    lazy val initialSize: Size = Size(120, 42)
    lazy val initialState: ControllerState = new IntroController
  }

  val engine = new GameEngine(ControllerStateMachine, terminal)

  engine.gameLoop()
}


