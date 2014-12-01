package controller

import console.model.World
import me.mtrupkin.console.control._
import me.mtrupkin.console.controller.ControllerStateMachine
import me.mtrupkin.console.layout._
import me.mtrupkin.console.screen.ConsoleKey
import me.mtrupkin.widget.ListWidget

trait Intro { self: ControllerStateMachine =>
  class IntroController extends ControllerState  {
    val window = new Composite("window") {
      override def keyPressed(key: ConsoleKey) {
        import scala.swing.event.Key._
        key.keyValue match {
          case Escape => exitGame()
          case _ =>
        }
        super.keyPressed(key)
      }
    }
    window.layout = Layout.FILL

    var elapsed = 0
    val listWidget = new ListWidget(List("New Game" -> newGame,"Load Game" -> loadGame, "Options" -> options, "Exit" -> exitGame))

    def newGame(): Unit = changeState(new GameController(World()))

    def loadGame(): Unit = flipState(new LoadGameController)

    def options(): Unit = flipState(new OptionsController)

    def exitGame(): Unit = {
      closed = true
    }

    val listBoarder = new Composite(name = "list-border", border = Border.DOUBLE)
    listBoarder.layout = Some(Layout(None, Pos.CENTER))
    listBoarder.addControl(listWidget)
    window.addControl(listBoarder)

    def update(elapsed: Int): Unit = {
      this.elapsed += elapsed
    }
  }
}
