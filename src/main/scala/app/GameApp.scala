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

object ControllerStateMachine extends ControllerStateMachine {
  lazy val initialSize: Size = Size(120, 42)
  lazy val initialState: ControllerState = new GameController(new World)
}

// TODO: fix right grab for vertical layout
object GameApp extends App {
  val size = Size(120, 42)
  val terminal = new SwingTerminal(size, "App")
  //val world = new World()
  //val controller = new GameController(world)

  val engine = new GameEngine(ControllerStateMachine, terminal)

  engine.gameLoop()
}


