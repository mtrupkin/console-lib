package org.flagship.game

import app.IntroController
import org.flagship.console.screen._

import org.flagship.console.terminal.Terminal
import org.flagship.console.Size
import state.{StateMachine}

/**
 * User: mtrupkin
 * Date: 7/5/13
 */

class GameEngine(size: Size, terminal: Terminal) extends StateMachine {
  type StateType = Controller

  trait Controller extends State {
    def update(elapsed: Int): Unit
    def render(screen: Screen): Unit
    def keyPressed(key: ConsoleKey): Unit
  }

  val updatesPerSecond = 100
  val updateRate = (1f / updatesPerSecond) * 1000
  val screen = Screen(size)

  def completed(): Boolean = terminal.closed

  override def update(elapsedTime: Int) {
    if (!completed()) {
      super.update(elapsedTime)
    }
  }

  def render() {
    if (!completed()) {
      currentState.render(screen)
      terminal.render(screen)
    }
  }

  def processInput() {
    for (key <- terminal.key) {
      currentState.keyPressed(key)
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
        currentState.update(updateRate.toInt)
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
