package controller

import console.model.{Agent, World}
import me.mtrupkin.console.control._
import me.mtrupkin.console.controller.ControllerStateMachine
import me.mtrupkin.console.layout._
import me.mtrupkin.console.screen.{ConsoleKey, ConsoleKeyModifier, Screen}
import me.mtrupkin.geometry.{Point, Size}
import widget.MapWidget

// Created on 11/6/2014.

trait Game { self: ControllerStateMachine =>
  class GameController(val world: World) extends ControllerState {
    val window = new Composite(name = "window", layoutFlow = Orientation.VERTICAL) {
      override def keyPressed(key: ConsoleKey) {
        import scala.swing.event.Key._
        key match {
          case ConsoleKey(Q, ConsoleKeyModifier.Control) => {
            changeState(new IntroController)
          }
          case ConsoleKey(X, ConsoleKeyModifier.Control) => {
            closed = true
          }
          case ConsoleKey(k, _) => k match {
            case W | Up => world.act(Point.Up)
            case A | Left => world.act(Point.Left)
            case S | Down => world.act(Point.Down)
            case D | Right => world.act(Point.Right)
            case Enter => ???
            case Escape => flipState(new EscapeMenuController(world))
            case _ =>
          }
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

    val mapPanel = new MapWidget(world.tileMap) {
      def onSelection(): Unit = {}

      def renderAgent(screen: Screen, agent: Agent): Unit = {
        val p = agent.position
        screen.write(p.x, p.y, agent.sc.c)
      }

      override def render(screen: Screen): Unit = {
        super.render(screen)
        renderAgent(screen,world.player)
        for (a <- world.agents) {
          renderAgent(screen, a)
        }
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
        screen.write(s"STR: ${world.player.str}")
      }
    }
    val label3 = new Control {
      override def minSize = Size(20, 1)
      override def render(screen: Screen): Unit = {
        screen.write(s"DEX: ${world.player.dex}")
      }
    }
    val label4 = new Control {
      override def minSize = Size(20, 1)
      override def render(screen: Screen): Unit = {
        screen.write(s"INT: ${world.player.int}")
      }
    }

    window.addControl(topPanel)
    window.addControl(bottomPanel)

    topPanel.addControls(Seq(mainPanel, detailPanel))
    detailPanel.addControls(Seq(label1,label2,label3,label4))

    def update(elapsed: Int) {
      world.update(elapsed)
    }
  }
}
