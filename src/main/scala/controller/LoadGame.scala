package controller

import console.model.World
import me.mtrupkin.console.control.{Border, Composite}
import me.mtrupkin.console.controller.ControllerStateMachine
import me.mtrupkin.console.layout.{Pos, Layout, Orientation}
import me.mtrupkin.console.screen.{ConsoleKeyModifier, ConsoleKey}
import me.mtrupkin.geometry.Point
import me.mtrupkin.terminal.Input
import me.mtrupkin.widget.IndexListWidget
import model.Saves

/**
 * Created by mtrupkin on 11/29/2014.
 */
trait LoadGame { self: ControllerStateMachine =>
    class LoadGameController extends ControllerState {

      val window = new Composite(name = "window", layoutFlow = Orientation.VERTICAL) {
        override def keyPressed(key: ConsoleKey) {
          import scala.swing.event.Key._
          key.keyValue match {
            case Escape => revertState()
            case _ => super.keyPressed(key)
          }
        }
      }

      val listWidget = new IndexListWidget(Saves.names, slot)
      val listBoarder = new Composite(name = "list-border", border = Border.DOUBLE)
      listBoarder.layout = Some(Layout(None, Pos.CENTER))
      listBoarder.addControl(listWidget)
      window.addControl(listBoarder)

      override def update(elapsed: Int): Unit = {}

      def slot(i: Int): Unit = {
        changeState(new GameController(Saves.loadGame(i)))
      }
    }
}
