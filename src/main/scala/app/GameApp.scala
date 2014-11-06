package org.flagship.console.app

/**
 * User: mtrupkin
 * Date: 11/29/13
 */


import app.GameEngine
import org.flagship.console.Size
import org.flagship.console.terminal.SwingTerminal


// TODO: fix right grab for vertical layout
object GameApp extends App {
  val size = Size(120, 42)
  val terminal = new SwingTerminal(size, "App")
  //val world = new World()
  //val controller = new GameController(world)

  val engine = new GameEngine(size, terminal)

  engine.gameLoop()
}


