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
      val items: List[String] = List("New", "Load", "Options", "Exit")
      val longest: String = items.reduceLeft((a,b) => if(a.length>b.length) a else b)

      override def minSize = Size(2 + longest.length, 2 + items.size)

      override def render(screen: Screen): Unit = {
        for ((item, i) <- items.zipWithIndex) {
          screen.write(0, i, item)
        }

        //items.foreach(s => screen.write(s"${s}"))
        //screen.write(s"${elapsed}")
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
