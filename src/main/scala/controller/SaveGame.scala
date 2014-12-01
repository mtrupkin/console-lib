package controller

import java.io.File
import java.nio.file.{Path, Files, Paths}

import console.model.World
import me.mtrupkin.console.control.{Border, Composite}
import me.mtrupkin.console.controller.ControllerStateMachine
import me.mtrupkin.console.layout.{Pos, Layout, Orientation}
import me.mtrupkin.console.screen.ConsoleKey
import me.mtrupkin.widget.{IndexListWidget, ListWidget}
import model.Saves


/**
 * Created by mtrupkin on 11/29/2014.
 */

trait SaveGame { self: ControllerStateMachine =>
  class SaveGameController(val world: World) extends ControllerState {

    val window = new Composite(name = "window", layoutFlow = Orientation.VERTICAL) {
      override def keyPressed(key: ConsoleKey) {
        import scala.swing.event.Key._
        key.keyValue match {
          case Escape => revertState()
          case _ => super.keyPressed(key)
        }
      }
    }

    val listWidget = new IndexListWidget("New Save" :: Saves.names, slot)
    val listBoarder = new Composite(name = "list-border", border = Border.DOUBLE)
    listBoarder.layout = Some(Layout(None, Pos.CENTER))
    listBoarder.addControl(listWidget)
    window.addControl(listBoarder)

    override def update(elapsed: Int): Unit = {}

    def slot(i: Int): Unit = {
      if (i == 0) {
        newSave()
      }
    }

    def newSave(): Unit = {
      val name = s"slot-${Saves.names().length}.sav"

      Saves.saveGame(name, world)
      revertState()
    }
  }
}
