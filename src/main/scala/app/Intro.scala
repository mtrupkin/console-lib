package console.app

import console.controller.ControllerStateMachine
import console.core.Size
import console.model.World
import console.control.Control
import console.screen.{ConsoleKey, Screen}

trait Intro { self: ControllerStateMachine =>
  class IntroController extends ControllerState  {

    var elapsed = 0

    val label1 = new Control {
      override def minSize = Size(20, 1)
      override def render(screen: Screen): Unit = {
        screen.write(s"${elapsed}")
      }
    }

    addControl(label1)

    // XXX
    var newGame = false

    def update(elapsed: Int): Unit = {
      this.elapsed += elapsed
      if (newGame) {
        val world = new World()
        val controller = new GameController(world)
        changeState(controller)
      }
    }

    override def keyPressed(key: ConsoleKey): Unit = {
      newGame = true
    }
  }
}
