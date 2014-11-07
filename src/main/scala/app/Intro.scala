package console.app

import console.controller.ControllerStateMachine
import console.core.{Point, Size}
import console.model.World
import console.control.Control
import console.screen.{ConsoleKey, Screen}

trait Intro { self: ControllerStateMachine =>
  class IntroController extends ControllerState  {
    var elapsed = 0

    val label1 = new Control {
      val items: List[String] = List("New Game", "Load Game", "Options", "Exit")
      val longest: String = items.reduceLeft((a,b) => if(a.length>b.length) a else b)

      override def minSize = Size(2 + longest.length, 2 + items.size)

      override def render(screen: Screen): Unit = {
        for ((item, i) <- items.zipWithIndex) {
          screen.write(1, i, item)
        }
      }
    }

    addControl(label1)

    def update(elapsed: Int): Unit = {
      this.elapsed += elapsed
    }

   def keyPressed(key: ConsoleKey): Unit = {
     val controller = new GameController(new World)
     changeState(controller)
    }

  }
}
