package console.app

import console.controller.ControllerStateMachine
import console.core.{Point, Size}
import console.model.World
import console.control._
import console.screen.{ConsoleKey, Screen}

trait Intro { self: ControllerStateMachine =>
  class IntroController extends ControllerState  {
    var elapsed = 0

    val list = new Control {
      var selected = 0
      val items: List[String] = List("New Game", "Load Game", "Options", "Exit")
      val longest: String = items.reduceLeft((a, b) => if (a.length > b.length) a else b)

      override def minSize = Size(2 + longest.length, items.size)

      override def render(screen: Screen): Unit = {
        for ((item, i) <- items.zipWithIndex) {
          val pad: Int = longest.length - item.length + 1//Math.ceil((longest.length - item.length) / 2.0).toInt
          if (i == selected) {
            screen.write(pad, i, s"$item+")
          } else {
            screen.write(pad, i, s"$item ")
          }
        }
      }

      override def mouseMoved(mouse: Point): Unit = {
        selected = mouse.y
      }

      override def keyPressed(key: ConsoleKey): Unit = {
        import scala.swing.event.Key._
        key.keyValue match {
          case Up => selected -= 1
          case Down => selected += 1
          case _ =>
        }
        val last = items.length -1
        if (selected < 0) selected = last else if ( selected > last ) selected = 0
      }

    }
    val listBoarder = new Composite(name = "list-border", border = Border.DOUBLE)
    //listBoarder.addControl(list)
    //addControl(listBoarder)
    addControl(list)

    def update(elapsed: Int): Unit = {
      this.elapsed += elapsed
    }
  }
}
