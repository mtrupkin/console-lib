package org.flagship.game

import org.flagship.console.screen._

import java.awt.event.{MouseEvent, MouseAdapter}
import org.flagship.console.terminal.Terminal
import org.flagship.console.Size


/**
 * User: mtrupkin
 * Date: 7/5/13
 */
trait GameEngine{
  val terminal: Terminal
  val framesPerSecond = 75
  val refreshRate = (1f / framesPerSecond) * 1000
  val screen: Screen

  def render(screen: Screen)
  def keyPressed(key: ConsoleKey)
  def update

  def completed(): Boolean = terminal.closed

  def render() {
    render(screen)
    terminal.render(screen)
  }

  def processInput() {
    for (key <- terminal.key) {
      keyPressed(key)
      terminal.key = None
    }
  }

  def gameLoop() {
    var lastUpdateTime = System.currentTimeMillis()

    while (!completed()) {
      val currentTime = System.currentTimeMillis()
      val elapsedTime = currentTime - lastUpdateTime

      if (elapsedTime > refreshRate) {
        lastUpdateTime = currentTime
        processInput
        update
      }

      render
    }
  }
}
