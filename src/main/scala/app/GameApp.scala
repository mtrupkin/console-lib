package console.app

/**
 * User: mtrupkin
 * Date: 11/29/13
 */

import console.engine.GameEngine
import console.terminal.SwingTerminal
import console.controller.ControllerStateMachine
import me.mtrupkin.geometry.Size
import me.trupkin.tile.ScreenMap


// TODO: fix right grab for vertical layout
object GameApp extends App {
  lazy val size = Size(120, 42)

  object ControllerStateMachine extends ControllerStateMachine {
    lazy val initialSize: Size = Size(120, 42)
    lazy val initialState: ControllerState = new IntroController
  }

  val terminal = new SwingTerminal(size, "Danger Room")
  val engine = new GameEngine(ControllerStateMachine, terminal)
  engine.gameLoop()

//  val screenMap2 = ScreenMap.readXP("C:\\dev\\REXPaint-R9\\images\\test2.xp")
//  ScreenMap.writeConsole("C:\\dev\\REXPaint-R9\\images\\test2.xpm", screenMap2)
}


