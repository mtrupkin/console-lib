package console.engine

import console.controller.ControllerStateMachine
import console.terminal.Terminal

/**
 * User: mtrupkin
 * Date: 7/5/13
 */

class GameEngine(controller: ControllerStateMachine, terminal: Terminal)  {
  val updatesPerSecond = 100
  val updateRate = (1f / updatesPerSecond) * 1000

  def completed(): Boolean = terminal.closed

  def render() {
    if (!completed()) {
      val screen = controller.render()
      terminal.render(screen)
    }
  }

  def processInput() {
    for (key <- terminal.key) {
      controller.keyPressed(key)
      terminal.key = None
    }
    for (mouse <- terminal.mouse) {
      controller.mouseMoved(mouse)
      terminal.mouse = None
    }
    if (terminal.mouseExit) {
      controller.mouseExited()
      terminal.mouseExit = false
    }
  }

  def gameLoop() {
    var lastUpdateTime = System.currentTimeMillis()
    var fpsTimer = System.currentTimeMillis()
    var accumulator = 0.0
    var frames = 0
    var updates = 0

    while (!completed()) {
      val currentTime = System.currentTimeMillis()
      val elapsedTime = currentTime - lastUpdateTime
      lastUpdateTime = currentTime

      accumulator += elapsedTime

      while (accumulator >= updateRate) {
        updates += 1
        processInput
        controller.update(updateRate.toInt)

        accumulator -= updateRate
      }

      render
      frames += 1

      if ( currentTime - fpsTimer > 10000 ) {
        fpsTimer = System.currentTimeMillis()
        println(s"frames: $frames updates: $updates")
        frames = 0
        updates = 0
      }
    }
  }
}