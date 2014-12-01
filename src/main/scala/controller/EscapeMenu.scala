package controller

import console.model.World
import me.mtrupkin.console.control.{Border, Composite}
import me.mtrupkin.console.controller.ControllerStateMachine
import me.mtrupkin.console.layout.{Pos, Layout, Orientation}
import me.mtrupkin.console.screen.{ConsoleKey, RGBColor, Screen, ScreenChar}
import me.mtrupkin.widget.ListWidget

/**
 * Created by mtrupkin on 11/29/2014.
 */
trait EscapeMenu { self: ControllerStateMachine =>
    class EscapeMenuController(val world: World) extends ControllerState {
      val listWidget = new ListWidget(List("Resume" -> revertState, "Save Game" -> saveGame, "Load Game" -> loadGame, "Options" -> options, "Quit" -> quitGame))

      def saveGame(): Unit = {
        flipState(new SaveGameController(world))
      }

      def loadGame(): Unit = {
        changeState(new LoadGameController)
      }

      def options(): Unit = {
        changeState(new OptionsController)
      }

      def quitGame(): Unit = {
        changeState(new IntroController)
      }

      val window = new Composite(name = "window", layoutFlow = Orientation.VERTICAL) {
        override def keyPressed(key: ConsoleKey) {
          import scala.swing.event.Key._
          key.keyValue match {
            case Escape => revertState()
            case _ =>
          }
          super.keyPressed(key)
        }
      }

      override def render(screen: Screen): Unit = {
        previousState().render(screen)
        dim(screen)
        window.render(screen)
      }

      override def update(elapsed: Int): Unit = {
        previousState().update(elapsed / 10)
      }

      def dim(screen: Screen): Unit = {
        screen.transform((sc: ScreenChar) => new ScreenChar(sc.c, dim(sc.fg), dim(sc.bg)))
      }

      def dim(c: RGBColor): RGBColor = {
        val factor = 3
        new RGBColor(c.r/factor, c.g/factor, c.b/factor)
      }

      val listBoarder = new Composite(name = "list-border", border = Border.DOUBLE)
      listBoarder.layout = Some(Layout(None, Pos.CENTER))
      listBoarder.addControl(listWidget)
      window.addControl(listBoarder)
    }
}
