package org.flagship.game

import org.flagship.console.screen._

import java.awt.event.{MouseEvent, MouseAdapter}
import org.flagship.console.terminal.Terminal
import org.flagship.console.Size

/**
 * User: mtrupkin
 * Date: 7/5/13
 */
class GameEngine(size: Size, terminal: Terminal, controller: Controller) {
  val updatesPerSecond = 100
  val updateRate = (1f / updatesPerSecond) * 1000
  val screen = Screen(size)

  def completed(): Boolean = terminal.closed

  def render() {
    if (!completed()) {
      controller.render(screen)
      terminal.render(screen)
    }
  }

  def processInput() {
    for (key <- terminal.key) {
      controller.keyPressed(key)
      terminal.key = None
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

      if ( currentTime - fpsTimer > 1000 ) {
        fpsTimer = System.currentTimeMillis()
        println(s"frames: $frames updates: $updates")
        frames = 0
        updates = 0
      }
    }
  }
}