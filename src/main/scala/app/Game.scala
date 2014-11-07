package console.app

import console.controller.ControllerStateMachine
import console.core.{Size, Point}
import console.map.MapWidget
import console.model.World
import console.control.LayoutOp._
import console.control._
import console.screen.{ConsoleKey, Screen}

// Created on 11/6/2014.

trait Game { self: ControllerStateMachine =>
  class GameController(val world: World) extends ControllerState {

    val topPanel = new Composite(name = "topPanel", layoutFlow = LayoutFlow.HORIZONTAL, border = new Border(box = Box.SINGLE_TEE_BOTTOM, divider = Divider.DOUBLE))//border = Border.SINGLE)
    topPanel.layout = Layout(right = GRAB, bottom = NONE)

    val bottomPanel = new Composite(name = "bottomPanel", border = Border.SINGLE_SANS_TOP)
    bottomPanel.layout = Layout(right = GRAB, bottom = GRAB)

    val mainBorder = new Border(box = Box.DOUBLE, sides = BorderSides(left = false, top = false, bottom = false))
    val mainPanel = new Composite(name = "mainPanel")
    mainPanel.layout = Layout(right = NONE, bottom = NONE)

    val mapPanel = new MapWidget(world) {
      def onSelection = {
      }
    }
    mainPanel.addControl(mapPanel)

    val detailBorder = new Border(box = Box.DOUBLE, sides = BorderSides(right = false, top = false, bottom = false))
    val detailPanel = new Composite(name = "detailPanel", layoutFlow = LayoutFlow.VERTICAL)
    detailPanel.layout = Layout(right = GRAB, bottom = GRAB)


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
    detailPanel.addControl(label1)
    detailPanel.addControl(label2)
    detailPanel.addControl(label3)

    topPanel.addControl(mainPanel)
    topPanel.addControl(detailPanel)

    addControl(topPanel)
    addControl(bottomPanel)

    def update(elapsed: Int) {
      world.update(elapsed)

      if (endGame) {
        changeState(new IntroController)
      }
    }

    //
    var endGame = false

    def keyPressed(key: ConsoleKey) {
      import scala.swing.event.Key._

      val k = key.keyValue
      k match {
        case W | Up => world.player.move(Point.Up)
        case A | Left => world.player.move(Point.Left)
        case S | Down => world.player.move(Point.Down)
        case D | Right => world.player.move(Point.Right)
        case Enter => ???
        case Escape => endGame = true
        case _ =>
      }
    }

  }
}
