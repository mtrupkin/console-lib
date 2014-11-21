package console.app

import console.controller.ControllerStateMachine
import console.map.MapWidget
import console.model.World
import me.mtrupkin.console.control._
import console.screen.{ConsoleKey, Screen}
import me.mtrupkin.geometry.{Point, Size}
import me.mtrupkin.console.layout._

// Created on 11/6/2014.

trait Game { self: ControllerStateMachine =>
  class GameController(val world: World) extends ControllerState {
    val window = new Composite(name = "window", layoutFlow = Orientation.VERTICAL) {
      override def keyPressed(key: ConsoleKey) {
        import scala.swing.event.Key._

        key.keyValue match {
          case W | Up => world.player.move(Point.Up)
          case A | Left => world.player.move(Point.Left)
          case S | Down => world.player.move(Point.Down)
          case D | Right => world.player.move(Point.Right)
          case Enter => ???
          case Escape => changeState(new IntroController)
          case _ =>
        }
      }
    }

    val topPanel = new Composite(name = "topPanel", border = new Border(box = Box.SINGLE_TEE_BOTTOM, divider = Divider.DOUBLE))//border = Border.SINGLE)
    topPanel.layout = Layout.FILL_RIGHT

    val bottomPanel = new Composite(name = "bottomPanel", border = Border.SINGLE_SANS_TOP)
    bottomPanel.layout = Layout.FILL

    val mainBorder = new Border(box = Box.DOUBLE, sides = BorderSides(left = false, top = false, bottom = false))
    val mainPanel = new Composite(name = "mainPanel")

    val mapPanel = new MapWidget(world) {
      def onSelection = {
      }
    }
    mainPanel.addControl(mapPanel)

    val detailBorder = new Border(box = Box.DOUBLE, sides = BorderSides(right = false, top = false, bottom = false))
    val detailPanel = new Composite(name = "detailPanel", layoutFlow = Orientation.VERTICAL)
    detailPanel.layout = Layout.FILL


    val label1 = new Control {
      override def minSize = Size(20, 1)
      override def render(screen: Screen): Unit = {
        screen.write(s"${world.time}")
      }
    }
    val label2 = new Control {
      override def minSize = Size(20, 1)
      override def render(screen: Screen): Unit = {
        screen.write("label 2")
      }
    }
    val label3 = new Control {
      override def minSize = Size(20, 1)
      override def render(screen: Screen): Unit = {
        screen.write("label 3")
      }
    }

    window.addControl(topPanel)
    window.addControl(bottomPanel)

    topPanel.addControls(Seq(mainPanel, detailPanel))
    detailPanel.addControls(Seq(label1,label2,label3))

    def update(elapsed: Int) {
      world.update(elapsed)
    }
  }
}
