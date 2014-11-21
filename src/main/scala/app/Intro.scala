package console.app

import me.mtrupkin.console.layout._
import me.mtrupkin.geometry._
import console.controller.ControllerStateMachine
import console.model.World
import me.mtrupkin.console.control._
import console.screen.{RGBColor, ConsoleKey, Screen}

trait Intro { self: ControllerStateMachine =>
  class IntroController extends ControllerState  {
    val window = new Composite("window")

    var elapsed = 0

    val list = new Control {
      var selected = 0
      val items: List[(String, () => Unit)] = List("New Game" -> newGame, "Load Game" -> loadGame, "Options" -> options, "Exit" -> exitGame)
      val itemNames = items.map(_._1)
      val longest: String = itemNames.reduceLeft((a, b) => if (a.length > b.length) a else b)

      override def minSize = Size(2 + longest.length, items.size)

      override def render(screen: Screen): Unit = {
        for ((item, i) <- itemNames.zipWithIndex) {
          val pad: Int = longest.length - item.length + 1//Math.ceil((longest.length - item.length) / 2.0).toInt
          if (i == selected) {
            screen.fg = RGBColor.LightGrey
            screen.write(pad, i, s"$item ")
            screen.fg = RGBColor.White
          } else {
            screen.write(pad, i, s"$item ")
          }
        }
      }

      override def mouseMoved(mouse: Point): Unit = {
        selected = mouse.y
      }

      override def mouseClicked(mouse: Point): Unit = {
        println("mouse clicked")
        select()
      }

      override def keyPressed(key: ConsoleKey): Unit = {
        import scala.swing.event.Key._
        key.keyValue match {
          case Up => selected -= 1
          case Down => selected += 1
          case Enter => select
          case _ =>
        }
        val last = items.length -1
        if (selected < 0) selected = last else if ( selected > last ) selected = 0
      }

      def select(): Unit = {
        val (_, action) = items(selected)
        action()
      }

      def newGame(): Unit = {
        changeState(new GameController(new World))
      }

      def loadGame(): Unit = {
      }

      def options(): Unit = {
      }

      def exitGame(): Unit = {
        closed = true
      }
    }
    val listBoarder = new Composite(name = "list-border", border = Border.DOUBLE)
    listBoarder.layout = Some(Layout(None, Pos.H_CENTER))

    listBoarder.addControl(list)
    window.addControl(listBoarder)
    //addControl(list)

    def update(elapsed: Int): Unit = {
      this.elapsed += elapsed
    }
  }
}
