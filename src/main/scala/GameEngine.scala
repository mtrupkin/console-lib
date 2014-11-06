package org.flagship.game

import model.World
import org.flagship.console.control.LayoutOp._
import org.flagship.console.control.{Control, Layout, LayoutFlow, Composite}
import org.flagship.console.screen._

import org.flagship.console.terminal.Terminal
import org.flagship.console.Size
import state.{  StateMachine}
import app.{GameController, IntroController}

/**
 * User: mtrupkin
 * Date: 7/5/13
 */

object ControllerStateMachine extends StateMachine {
  type StateType = ControllerState
  val size = Size(120, 42)
  //def initialState: Controller = new IntroController
  def initialState: StateType = new GameController(new World)
  //initialState.onEnter()

  trait ControllerState extends State {
    val window = new Composite("MainWindow", LayoutFlow.VERTICAL)
    window.layout = Layout(right = GRAB, bottom = GRAB)
    def addControl(control: Control): Unit = window.addControl(control)

    def update(elapsed: Int): Unit
    def render(screen: Screen): Unit = window.render(screen)
    def keyPressed(key: ConsoleKey): Unit

    override def onEnter(): Unit = {
      window.arrange(size)
    }
  }

  def render(screen: Screen): Unit = {
    currentState.render(screen)
  }

  def keyPressed(key: ConsoleKey): Unit = {
    currentState.keyPressed(key: ConsoleKey)
  }
}





class GameEngine(size: Size, terminal: Terminal)  {
  val AppController = ControllerStateMachine
  val updatesPerSecond = 100
  val updateRate = (1f / updatesPerSecond) * 1000
  val screen = Screen(size)

  def completed(): Boolean = terminal.closed


  def render() {
    if (!completed()) {
      AppController.render(screen)
      terminal.render(screen)
    }
  }

  def processInput() {
    for (key <- terminal.key) {
      AppController.keyPressed(key)
      terminal.key = None
    }
  }

  def gameLoop() {
    var lastUpdateTime = System.currentTimeMillis()
    var fpsTimer = System.currentTimeMillis()
    var accumulator = 0.0
    var frames = 0
    var updates = 0

    while (!completed()) {
      val currentTime = System.currentTimeMillis()
      val elapsedTime = currentTime - lastUpdateTime
      lastUpdateTime = currentTime

      accumulator += elapsedTime

      while (accumulator >= updateRate) {
        updates += 1
        processInput
        AppController.update(updateRate.toInt)

        accumulator -= updateRate
      }

      render
      frames += 1

      if ( currentTime - fpsTimer > 1000 ) {
        fpsTimer = System.currentTimeMillis()
        println(s"frames: $frames updates: $updates")
        frames = 0
        updates = 0
      }
    }
  }
}
