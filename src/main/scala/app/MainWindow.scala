package org.flagship.console.app

import org.flagship.console.control.Composite
import org.flagship.console.Point
import org.flagship.console.Size

/**
 * Created by mtrupkin on 3/8/14.
 */
class MainWindow(size: Size) extends Composite(size, Some("Window")) {
    controls = Nil

    val mainPanel = new Composite(LayoutManager.VERTICAL)
    mainPanel.controlLayout = Layout(bottom = GRAB, right = GRAB)

    val topPanel = new Composite
    val messagePanel = new MessageView(game) with Border
    messagePanel.controlLayout = Layout(bottom = GRAB, right = GRAB)

    val mapPanel = new MapView(game) with Border
    val detailPanel = new Composite(LayoutManager.VERTICAL) with Border
    val shipStatusPanel = new ShipStatusView(game) with Border {
      override def minSize: Size = Size(25, 8)
    }
    val enemyShipPanel = new EnemyShipStatusView(game) with Border {
      override def minSize: Size = Size(25, 6)
    }
    val playerStatusPanel = new PlayerStatusView(game) with Border


    import LayoutData._

    topPanel.controlLayout = Layout(right = GRAB)
    detailPanel.controlLayout = Layout(bottom = GRAB, right = GRAB)
    shipStatusPanel.controlLayout = Layout(right = GRAB)
    enemyShipPanel.controlLayout = Layout(right = GRAB)
    playerStatusPanel.controlLayout = Layout(bottom = GRAB, right = GRAB)

    detailPanel.addControl(shipStatusPanel)
    detailPanel.addControl(enemyShipPanel)
    detailPanel.addControl(playerStatusPanel)

    topPanel.addControl(mapPanel)
    topPanel.addControl(detailPanel)

    mainPanel.addControl(topPanel)
    mainPanel.addControl(messagePanel)

    addControl(mainPanel)

    layout()

  override def keyPressed(key: ConsoleKey) {
    import scala.swing.event.Key._

    val k = key.keyValue
    k match {
      case W | Up => game.move(Point.Up)
      case A | Left => game.move(Point.Left)
      case S | Down => game.move(Point.Down)
      case D | Right => game.move(Point.Right)
      case Space => game.endMovement()
      case Enter => accept()
      case Escape => closed = true
      case _ =>
    }
  }

  def accept() {
    if (game.step == PhaseStepType.EndGame) {
      game = model.GameApp()
      initialize()
    }
  }

  override def update(elapsedTime: Int) {
    time += elapsedTime

    game.update(elapsedTime: Int)
  }

}
